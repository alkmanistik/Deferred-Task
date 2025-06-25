package com.alkmanistik.deferred_thread.controller;

import com.alkmanistik.deferred_thread.data.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.data.model.WorkerParams;
import com.alkmanistik.deferred_thread.request.StartWorkerRequest;
import com.alkmanistik.deferred_thread.service.WorkerManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
@Tag(name = "WorkerController", description = "Взаимодейсвие с выполнителем задач")
public class WorkerController {

    private final WorkerManager workerManager;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Запуск выполнителя",
            description = "Позволяет зарегистрировать выполнитель и настроить"
    )
    @ApiResponse(responseCode = "400", description = "Проблема с валидацией")
    @ApiResponse(responseCode = "409", description = "Выполнитель уже существует")
    public String startWorker(@Valid @RequestBody StartWorkerRequest request) {
        WorkerParams workerParams = new WorkerParams(
                request.getCategory(),
                request.getThreadNumber(),
                request.getTasksNumber()
        );

        RetryPolicyParam retryPolicy = new RetryPolicyParam(
                request.getRetryBase(),
                request.getRetryCount(),
                request.getMaxRetryDelay()
        );

        workerManager.init(workerParams, retryPolicy);
        return String.format("Worker for category '%s' started with %d threads",
                request.getCategory(), request.getThreadNumber());
    }

    @PostMapping("/stop/{category}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Уничтожение выполнителя"
    )
    @ApiResponse(responseCode = "400", description = "Проблема с валидацией")
    public String stopWorker(@Valid @PathVariable String category) {
        workerManager.destroy(category);
        return String.format("Worker for category '%s' stopped", category);
    }
}