package com.alkmanistik.deferred_thread.controller;

import com.alkmanistik.deferred_thread.request.ScheduleTaskRequest;
import com.alkmanistik.deferred_thread.request.TaskCancelRequest;
import com.alkmanistik.deferred_thread.response.TaskResponse;
import com.alkmanistik.deferred_thread.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "TaskController", description = "Взаимодейсвие с задачами")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/schedule")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Регистрация задачи",
            description = "Позволяет зарегистрировать задачу со временем её выполнения и параметрами запуска"
    )
    @ApiResponse(responseCode = "400", description = "Проблема с валидацией")
    public TaskResponse scheduleTask(@Valid @RequestBody ScheduleTaskRequest request) {
        return taskService.scheduleTask(request);
    }

    @PostMapping("/cancel/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Отмена задачи"
    )
    @ApiResponse(responseCode = "400", description = "Проблема с валидацией")
    @ApiResponse(responseCode = "409", description = "Задача уже не может быть отменена")
    public void cancelTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskCancelRequest request) {
        taskService.cancelTask(taskId, request);
    }

}
