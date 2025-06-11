package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.data.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.data.model.WorkerParams;

public interface WorkerManager {

    void init(WorkerParams workerParams, RetryPolicyParam retryParams);

    void destroy(String category);

}
