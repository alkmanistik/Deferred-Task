package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.request.ScheduleTaskRequest;
import com.alkmanistik.deferred_thread.request.TaskCancelRequest;
import com.alkmanistik.deferred_thread.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskManager taskManager;

    public TaskResponse scheduleTask(ScheduleTaskRequest request) {
        return new TaskResponse();
    }

    public void cancelTask(Long taskId, TaskCancelRequest request) {
        boolean cancelled = taskManager.cancel(request.getCategory(), taskId);
        if (!cancelled) {
            throw new TaskCancellationException("Task cannot be cancelled");
        }
    }
}
