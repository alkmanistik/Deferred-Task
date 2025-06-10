package com.alkmanistik.deferred_thread.controller;

import com.alkmanistik.deferred_thread.request.ScheduleTaskRequest;
import com.alkmanistik.deferred_thread.request.TaskCancelRequest;
import com.alkmanistik.deferred_thread.response.TaskResponse;
import com.alkmanistik.deferred_thread.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/schedule")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse scheduleTask(@Valid @RequestBody ScheduleTaskRequest request) {
        return taskService.scheduleTask(request);
    }

    @PostMapping("/cancel/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskCancelRequest request) {
        taskService.cancelTask(taskId, request);
    }

}
