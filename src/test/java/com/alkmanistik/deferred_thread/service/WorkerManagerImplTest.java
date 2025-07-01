package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.data.model.RetryPolicyParam;
import com.alkmanistik.deferred_thread.data.model.WorkerParams;
import com.alkmanistik.deferred_thread.exception.WorkerAlreadyExist;
import com.alkmanistik.deferred_thread.exception.WorkerNotFound;
import com.alkmanistik.deferred_thread.repository.CustomTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WorkerManagerImplTest {

    @Mock
    private CustomTaskRepository taskRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WorkerManagerImpl workerManager;

    @Test
    void init() {

        WorkerParams workerParams = new WorkerParams();
        workerParams.setCategory("email");
        workerParams.setTasksNumber(5);
        workerParams.setThreadNumber(3);

        RetryPolicyParam retryParams = new RetryPolicyParam();
        retryParams.setRetryCount(3);

        assertDoesNotThrow(() -> workerManager.init(workerParams, retryParams));

    }

    @Test
    void initCategoryAlreadyExist() {

        WorkerParams workerParams = new WorkerParams();
        workerParams.setCategory("email");
        workerParams.setTasksNumber(5);
        workerParams.setThreadNumber(3);

        RetryPolicyParam retryParams = new RetryPolicyParam();
        retryParams.setRetryCount(3);

        workerManager.init(workerParams, retryParams);

        assertThrows(WorkerAlreadyExist.class, () -> workerManager.init(workerParams, retryParams));

    }

    @Test
    void destroy() {

        WorkerParams workerParams = new WorkerParams();
        workerParams.setCategory("email");
        workerParams.setTasksNumber(5);
        workerParams.setThreadNumber(3);

        RetryPolicyParam retryParams = new RetryPolicyParam();
        retryParams.setRetryCount(3);

        String category = "email";

        workerManager.init(workerParams, retryParams);

        assertDoesNotThrow(() -> workerManager.destroy(category));

    }

    @Test
    void destroyNotFound() {

        String category = "email";

        assertThrows(WorkerNotFound.class, () -> workerManager.destroy(category));

    }

    @Test
    void destroyAll() {

        WorkerParams workerParams = new WorkerParams();
        workerParams.setCategory("email");
        workerParams.setTasksNumber(5);
        workerParams.setThreadNumber(3);

        RetryPolicyParam retryParams = new RetryPolicyParam();
        retryParams.setRetryCount(3);

        WorkerParams workerParamsSecond = new WorkerParams();
        workerParamsSecond.setCategory("image");
        workerParamsSecond.setTasksNumber(5);
        workerParamsSecond.setThreadNumber(3);

        RetryPolicyParam retryParamsSecond = new RetryPolicyParam();
        retryParamsSecond.setRetryCount(3);

        workerManager.init(workerParams, retryParams);
        workerManager.init(workerParamsSecond, retryParamsSecond);

        assertDoesNotThrow(() -> workerManager.destroyAll());

    }

}
