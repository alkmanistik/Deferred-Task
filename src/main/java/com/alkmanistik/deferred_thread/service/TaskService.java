package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.entity.enums.TaskStatus;
import com.alkmanistik.deferred_thread.exception.TaskCancellationException;
import com.alkmanistik.deferred_thread.exception.TaskClassNotFoundException;
import com.alkmanistik.deferred_thread.request.ScheduleTaskRequest;
import com.alkmanistik.deferred_thread.request.TaskCancelRequest;
import com.alkmanistik.deferred_thread.response.TaskResponse;
import com.alkmanistik.deferred_thread.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskManager taskManager;

    public TaskResponse scheduleTask(ScheduleTaskRequest request) {

        try{
            Class<? extends Task> taskClass = (Class<? extends Task>) Class.forName(request.getTaskClassName());

            long taskId = taskManager.schedule(
                    request.getCategory(),
                    (Class<Task>) taskClass,
                    request.getTaskParams(),
                    request.getScheduledTime()
            );

            return new TaskResponse(
                    taskId,
                    request.getCategory(),
                    TaskStatus.SCHEDULED,
                    request.getScheduledTime()
            );

        } catch (ClassNotFoundException e) {
            throw new TaskClassNotFoundException(request.getTaskClassName());
        }


    }

    public void cancelTask(Long taskId, TaskCancelRequest request) {
        boolean cancelled = taskManager.cancel(request.getCategory(), taskId);
        if (!cancelled) {
            throw new TaskCancellationException("Task cannot be cancelled");
        }
    }
}
