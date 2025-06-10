package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;
import com.alkmanistik.deferred_thread.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class WorkerManagerImpl implements WorkerManager {
    private final Map<String, Worker> workers = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void init(WorkerParams workerParams, RetryPolicyParam retryParams) {
        synchronized (lock) {
            String category = workerParams.getCategory();
            if (!workers.containsKey(category)) {
                Worker worker = new Worker(workerParams, retryParams, taskRepository, objectMapper);
                workers.put(category, worker);
                worker.start();
            }
        }
    }

    @Override
    public void destroy(String category) {
        synchronized (lock) {
            Worker worker = workers.remove(category);
            if (worker != null) {
                worker.stop();
            }
        }
    }
}