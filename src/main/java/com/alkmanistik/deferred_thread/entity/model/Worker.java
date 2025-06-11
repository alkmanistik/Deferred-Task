package com.alkmanistik.deferred_thread.entity.model;

import com.alkmanistik.deferred_thread.entity.TaskEntity;
import com.alkmanistik.deferred_thread.entity.enums.TaskStatus;
import com.alkmanistik.deferred_thread.repository.TaskRepository;
import com.alkmanistik.deferred_thread.task.EmailProcessingTask;
import com.alkmanistik.deferred_thread.task.ImageProcessingTask;
import com.alkmanistik.deferred_thread.task.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class Worker {
    private final WorkerParams workerParams;
    private final RetryPolicyParam retryPolicyParam;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;
    private ExecutorService executorService;
    private volatile boolean running;

    public Worker(WorkerParams workerParams, RetryPolicyParam retryPolicyParam,
                  TaskRepository taskRepository, ObjectMapper objectMapper) {
        this.workerParams = workerParams;
        this.retryPolicyParam = retryPolicyParam;
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
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
                    taskRepository.save(task);
                    taskQueue.put(task);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processTaskFromQueue() {
        while (running) {
            try {
                TaskEntity task = taskQueue.poll(500, TimeUnit.MILLISECONDS);
                if (task != null) {
                    processTask(task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.running = false;
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Transactional
    protected List<TaskEntity> fetchTasks() {
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findDeferred(
                workerParams.getCategory(),
                TaskStatus.SCHEDULED,
                now,
                workerParams.getTasksNumber());
    }

    @SuppressWarnings("unchecked")
    private void processTask(TaskEntity task) {
        try {
            Map<String, Object> params = objectMapper.readValue(
                    task.getTaskParamsJson(),
                    new TypeReference<HashMap<String, Object>>() {
                    });

            Class<?> taskClass = Class.forName(task.getTaskClassName());
            Task taskInstance;

            if (EmailProcessingTask.class.isAssignableFrom(taskClass)) {
                taskInstance = new EmailProcessingTask(params);
            } else if (ImageProcessingTask.class.isAssignableFrom(taskClass)) {
                taskInstance = new ImageProcessingTask(params);
            } else {
                taskInstance = (Task) taskClass.getConstructor(Map.class).newInstance(params);
            }

            boolean success = executeWithRetries(taskInstance, task);

            task.setStatus(success ? TaskStatus.COMPLETED : TaskStatus.FAILED);
        } catch (Exception e) {
            task.setStatus(TaskStatus.FAILED);
            e.printStackTrace();
        } finally {
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
        }
    }

    private boolean executeWithRetries(Task task, TaskEntity taskEntity) {
        int attempts = 0;
        while (attempts <= retryPolicyParam.getRetryCount()) {
            try {
                task.run();
                return true;
            } catch (Exception e) {
                attempts++;
                if (attempts > retryPolicyParam.getRetryCount()) {
                    return false;
                }
                try {
                    Thread.sleep((long) Math.pow(2, attempts) * 1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }
}