package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;
import org.springframework.stereotype.Service;

@Service
public class WorkManagerImpl implements WorkManager {

    @Override
    public void init(WorkerParams workerParams, RetryPolicyParam retryParams) {

    }

    @Override
    public void destroy(String category) {

    }
}
