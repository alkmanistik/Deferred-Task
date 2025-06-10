package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;

public interface WorkerManager {

    void init(WorkerParams workerParams, RetryPolicyParam retryParams);

    void destroy(String category);

}
