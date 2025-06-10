package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.entity.TaskEntity;
import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;
import com.alkmanistik.deferred_thread.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WorkerManagerImpl implements WorkerManager {

    private final Map<String, Map<Long, TaskEntity>> workerContexts = new ConcurrentHashMap();
    private final TaskRepository taskRepository;
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    private WorkerParams workerParams;
    private RetryPolicyParam retryPolicyParam;


    @Override
    public void init(WorkerParams workerParams, RetryPolicyParam retryParams) {
        this.workerParams = workerParams;
        this.retryPolicyParam = retryParams;
    }

    @Override
    public void destroy(String category) {
        workerContexts.remove(category);
    }

}
