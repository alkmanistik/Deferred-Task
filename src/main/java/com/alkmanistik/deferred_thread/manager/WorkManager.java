package com.alkmanistik.deferred_thread.manager;

import com.alkmanistik.deferred_thread.entity.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.entity.model.WorkerParams;

public interface WorkManager {

    void init(WorkerParams workerParams, RetryPolicyParam retryParams);

    void destroy(String category);

}
