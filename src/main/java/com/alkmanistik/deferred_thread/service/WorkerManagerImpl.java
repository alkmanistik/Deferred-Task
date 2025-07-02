package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.model.data.RetryPolicyParam;
import com.alkmanistik.deferred_thread.model.Worker;
import com.alkmanistik.deferred_thread.model.data.WorkerParams;
import com.alkmanistik.deferred_thread.exception.WorkerAlreadyExist;
import com.alkmanistik.deferred_thread.exception.WorkerNotFound;
import com.alkmanistik.deferred_thread.repository.CustomTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
@RequiredArgsConstructor
public class WorkerManagerImpl implements WorkerManager {
    private final Map<String, Worker> workers = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private final ObjectMapper objectMapper;
    private final CustomTaskRepository customTaskRepository;

    @Override
    public void init(WorkerParams workerParams, RetryPolicyParam retryParams) {
        synchronized (lock) {
            String category = workerParams.getCategory();
            if (!workers.containsKey(category)) {
                Worker worker = new Worker(workerParams, retryParams, objectMapper, customTaskRepository);
                workers.put(category, worker);
                worker.start();
                log.info("Worker started with workerParam={}, retryParams={}", workerParams, retryParams);
            }
            else{
                log.error("Failed to create worker: Trying create worker already exist");
                throw new WorkerAlreadyExist("Worker already exist with category: " + category);
            }
        }
    }

    @Override
    public void destroy(String category) {
        synchronized (lock) {
            Worker worker = workers.remove(category);
            if (worker != null) {
                worker.stop();
                log.info("Worker stopped with category: {}", category);
            }
            else{
                log.error("Failed to destroy worker: Trying destroy not exist worker");
                throw new WorkerNotFound("Worker not found with category: " + category);
            }
        }
    }

    @PreDestroy
    public void destroyAll() {
        workers.keySet().forEach(this::destroy);
    }

}