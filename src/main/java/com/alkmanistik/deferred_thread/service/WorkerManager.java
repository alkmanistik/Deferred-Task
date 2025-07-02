package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.model.data.RetryPolicyParam;
import com.alkmanistik.deferred_thread.model.data.WorkerParams;

public interface WorkerManager {

    void init(WorkerParams workerParams, RetryPolicyParam retryParams);

    void destroy(String category);

}
