package com.alkmanistik.deferred_thread.data.model;

import com.alkmanistik.deferred_thread.data.entity.TaskEntity;
import com.alkmanistik.deferred_thread.data.enums.TaskStatus;
import com.alkmanistik.deferred_thread.repository.CustomTaskRepository;
import com.alkmanistik.deferred_thread.task.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class Worker {
    private final WorkerParams workerParams;
    private final RetryPolicyParam retryPolicyParam;
    private final ObjectMapper objectMapper;
    private final CustomTaskRepository customTaskRepository;
    private ExecutorService executorService;
    private volatile boolean running;

    public Worker(WorkerParams workerParams, RetryPolicyParam retryPolicyParam, ObjectMapper objectMapper, CustomTaskRepository customTaskRepository) {
        this.workerParams = workerParams;
        this.retryPolicyParam = retryPolicyParam;
        this.objectMapper = objectMapper;
        this.customTaskRepository = customTaskRepository;
        this.workerParams.setThreadNumber(workerParams.getThreadNumber() + 1);
    }

    private final BlockingQueue<TaskEntity> taskQueue = new LinkedBlockingQueue<>();

    public void start() {
        this.running = true;
        this.executorService = Executors.newFixedThreadPool(workerParams.getThreadNumber());

        executorService.submit(this::fetchAndQueueTasks);

        for (int i = 0; i < workerParams.getThreadNumber(); i++) {
            executorService.submit(this::processTaskFromQueue);
        }
    }

    private void fetchAndQueueTasks() {
        while (running) {
            try {
                List<TaskEntity> tasks = fetchTasks();
                for (TaskEntity task : tasks) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                    customTaskRepository.save(task);
                    taskQueue.put(task);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error fetching tasks", e);
            }
        }
    }

    private void processTaskFromQueue() {
        while (running) {
            try {
                TaskEntity task = taskQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (task != null) {
                    processTask(task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error fetching tasks", e);
            }
        }
    }

    public void stop() {
        this.running = false;
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        int resetCount = customTaskRepository.resetInProgressTasks(workerParams.getCategory());
        if (resetCount!=0){
            log.info("Reset {} IN_PROGRESS tasks to SCHEDULED", resetCount);
        }
    }

    protected List<TaskEntity> fetchTasks() {
        LocalDateTime now = LocalDateTime.now();
        return customTaskRepository.findDeferred(workerParams.getCategory(), TaskStatus.SCHEDULED, now, workerParams.getTasksNumber());
    }

    private void processTask(TaskEntity task) {
        try {
            Map<String, Object> params = objectMapper.readValue(task.getTaskParamsJson(), new TypeReference<HashMap<String, Object>>() {
            });

            Class<?> taskClass = Class.forName(task.getTaskClassName());
            Task taskInstance;

            taskInstance = (Task) taskClass.getConstructor(Map.class).newInstance(params);

            boolean success = executeTask(taskInstance);

            if (!success) {
                handleFailedTask(task);
                return;
            }

            task.setStatus(TaskStatus.COMPLETED);
            log.info("Task complete with id={}, category={}", task.getId(), task.getCategory());
        } catch (Exception e) {
            log.error("Task processing failed", e);
            task.setStatus(TaskStatus.FAILED);
            handleFailedTask(task);
        } finally {
            task.setUpdatedAt(LocalDateTime.now());
            customTaskRepository.save(task);
        }
    }

    private boolean executeTask(Task task) {
        try {
            task.run();
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    private void handleFailedTask(TaskEntity task) {
        if (task.getRetryCount() < retryPolicyParam.getRetryCount()) {
            scheduleRetry(task);
        } else {
            task.setStatus(TaskStatus.FAILED);
        }
    }

    private void scheduleRetry(TaskEntity task) {
        int nextRetryCount = task.getRetryCount() + 1;
        Duration delay = calculateRetryDelay(nextRetryCount);

        TaskEntity retryTask = new TaskEntity();
        retryTask.setCategory(task.getCategory());
        retryTask.setTaskClassName(task.getTaskClassName());
        retryTask.setTaskParamsJson(task.getTaskParamsJson());
        retryTask.setScheduledTime(LocalDateTime.now().plus(delay));
        retryTask.setRetryCount(nextRetryCount);
        retryTask.setStatus(TaskStatus.SCHEDULED);

        customTaskRepository.save(retryTask);
        task.setStatus(TaskStatus.FAILED);
    }

    private Duration calculateRetryDelay(int retryAttempt) {
        // y = min(a^x, maxDelay)
        double delaySeconds = Math.pow(retryPolicyParam.getRetryTime(), retryAttempt);
        Duration calculatedDelay = Duration.ofSeconds((long) delaySeconds);

        return calculatedDelay.compareTo(retryPolicyParam.getMaxDelay()) > 0
                ? retryPolicyParam.getMaxDelay()
                : calculatedDelay;
    }

}