package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.data.enums.TaskStatus;
import com.alkmanistik.deferred_thread.exception.TaskCancellationException;
import com.alkmanistik.deferred_thread.exception.TaskClassNotFoundException;
import com.alkmanistik.deferred_thread.exception.TaskParameterValidationException;
import com.alkmanistik.deferred_thread.request.ScheduleTaskRequest;
import com.alkmanistik.deferred_thread.request.TaskCancelRequest;
import com.alkmanistik.deferred_thread.response.TaskResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskManager taskManager;

    @InjectMocks
    private TaskService taskService;

    @Test
    void scheduleTask() {

        ScheduleTaskRequest request = new ScheduleTaskRequest();
        request.setCategory("email");
        request.setTaskClassName("com.alkmanistik.deferred_thread.task.EmailProcessingTask");
        request.setTaskParams(Map.of(
                "email", "erik.fattakhov.02@mail.ru",
                "message", "Hello"
        ));
        request.setScheduledTime(LocalDateTime.parse("2025-06-10T22:39:39.492"));

        when(taskManager.schedule(anyString(), any(), anyMap(), any(LocalDateTime.class))).thenReturn(24L);

        TaskResponse response = taskService.scheduleTask(request);

        assertEquals(response.getTaskId(), 24L);
        assertEquals(response.getCategory(), "email");
        assertEquals(response.getStatus(), TaskStatus.SCHEDULED);
        assertEquals(response.getScheduledTime(), LocalDateTime.parse("2025-06-10T22:39:39.492"));

    }

    @Test
    void scheduleTaskWithWrongClassName() {
        ScheduleTaskRequest request = new ScheduleTaskRequest();
        request.setCategory("email");
        request.setTaskClassName("com.alkmanistik.deferred_thread.task.RandomNotExistingTask");
        request.setTaskParams(Map.of(
                "email", "erik.fattakhov.02@mail.ru",
                "message", "Hello"
        ));
        request.setScheduledTime(LocalDateTime.parse("2025-06-10T22:39:39.492"));

        assertThrows(TaskClassNotFoundException.class, () -> taskService.scheduleTask(request));
    }

    @Test
    void scheduleTaskWithWrongParameter() {
        ScheduleTaskRequest request = new ScheduleTaskRequest();
        request.setCategory("email");
        request.setTaskClassName("com.alkmanistik.deferred_thread.task.EmailProcessingTask");
        request.setTaskParams(Map.of(
                "randomValue", "erik.fattakhov.02@mail.ru",
                "message", "Hello"
        ));
        request.setScheduledTime(LocalDateTime.parse("2025-06-10T22:39:39.492"));

        assertThrows(TaskParameterValidationException.class, () -> taskService.scheduleTask(request));
    }

    @Test
    public void cancelTaskSuccess() {

        Long taskId = 1L;
        TaskCancelRequest taskCancelRequest = new TaskCancelRequest();
        taskCancelRequest.setCategory("email");

        when(taskManager.cancel(anyString(), anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> taskService.cancelTask(taskId, taskCancelRequest));
    }

    @Test
    public void cancelTaskCancelled() {

        Long taskId = 1L;
        TaskCancelRequest taskCancelRequest = new TaskCancelRequest();
        taskCancelRequest.setCategory("email");

        when(taskManager.cancel(anyString(), anyLong())).thenReturn(false);

        assertThrows(TaskCancellationException.class, () -> taskService.cancelTask(taskId, taskCancelRequest));
    }

}
