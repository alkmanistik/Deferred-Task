package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.model.anotation.TaskParams;
import com.alkmanistik.deferred_thread.model.enums.TaskStatus;
import com.alkmanistik.deferred_thread.exception.TaskCancellationException;
import com.alkmanistik.deferred_thread.exception.TaskClassNotFoundException;
import com.alkmanistik.deferred_thread.exception.TaskParameterValidationException;
import com.alkmanistik.deferred_thread.dto.request.ScheduleTaskRequest;
import com.alkmanistik.deferred_thread.dto.request.TaskCancelRequest;
import com.alkmanistik.deferred_thread.dto.response.TaskResponse;
import com.alkmanistik.deferred_thread.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskManager taskManager;

    public TaskResponse scheduleTask(ScheduleTaskRequest request) {

        try{
            Class<? extends Task> taskClass = (Class<? extends Task>) Class.forName(request.getTaskClassName());

            validateTaskParams(taskClass, request.getTaskParams());

            long taskId = taskManager.schedule(
                    request.getCategory(),
                    (Class<Task>) taskClass,
                    request.getTaskParams(),
                    request.getScheduledTime()
            );

            log.info(
                    "Created task with id={}, category={}, scheduledTime={}",
                    taskId,
                    request.getCategory(),
                    request.getScheduledTime()
            );

            return new TaskResponse(
                    taskId,
                    request.getCategory(),
                    TaskStatus.SCHEDULED,
                    request.getScheduledTime()
            );

        } catch (ClassNotFoundException e) {
            log.error(
                    "Failed to schedule task: class not found={}",
                    request.getTaskClassName()
            );
            throw new TaskClassNotFoundException(request.getTaskClassName());
        }


    }

    public void cancelTask(Long taskId, TaskCancelRequest request) {
        boolean cancelled = taskManager.cancel(request.getCategory(), taskId);
        if (!cancelled) {
            log.error(
                    "Failed to cancel task: class id={} category={}",
                    taskId,
                    request.getCategory()
            );
            throw new TaskCancellationException("Task cannot be cancelled");
        }else{
            log.info("Task cancelled with id={} category={}", taskId, request.getCategory());
        }
    }

    private void validateTaskParams(Class<? extends Task> taskClass, Map<String, Object> params) {
        TaskParams annotation = taskClass.getAnnotation(TaskParams.class);
        if (annotation != null) {
            for (String requiredParam : annotation.required()) {
                if (!params.containsKey(requiredParam)) {
                    throw new TaskParameterValidationException(
                            "Missing required parameter: " + requiredParam +
                                    " for task: " + taskClass.getSimpleName());
                }

                Object value = params.get(requiredParam);
                if (value == null) {
                    throw new TaskParameterValidationException(
                            "Parameter " + requiredParam + " cannot be null" +
                                    " for task: " + taskClass.getSimpleName());
                }
            }
        }
    }
}
