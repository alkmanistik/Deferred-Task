package com.alkmanistik.deferred_thread.controller;

import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;
import com.alkmanistik.deferred_thread.request.StartWorkerRequest;
import com.alkmanistik.deferred_thread.service.WorkerManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerManager workerManager;

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.OK)
    public String startWorker(@RequestBody StartWorkerRequest request) {
        WorkerParams workerParams = new WorkerParams(
                request.getCategory(),
                request.getThreadNumber(),
                request.getTasksNumber()
        );

        RetryPolicyParam retryPolicy = new RetryPolicyParam(request.getRetryCount());

        workerManager.init(workerParams, retryPolicy);
        return String.format("Worker for category '%s' started with %d threads",
                request.getCategory(), request.getThreadNumber());
    }

    @PostMapping("/stop/{category}")
    @ResponseStatus(HttpStatus.OK)
    public String stopWorker(@PathVariable String category) {
        workerManager.destroy(category);
        return String.format("Worker for category '%s' stopped", category);
    }
}