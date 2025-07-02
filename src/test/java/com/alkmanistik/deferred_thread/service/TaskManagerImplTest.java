package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.model.entity.TaskEntity;
import com.alkmanistik.deferred_thread.repository.CustomTaskRepository;
import com.alkmanistik.deferred_thread.task.EmailProcessingTask;
import com.alkmanistik.deferred_thread.task.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskManagerImplTest {

    @Mock
    private CustomTaskRepository taskRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TaskManagerImpl taskManager;

    @Test
    void scheduleSuccessful() {

        String category = "email";
        Class<? extends Task> taskClass = EmailProcessingTask.class;
        Map<String, Object> map = Map.of(
                "email", "erik.fattakhov.02@mail.ru",
                "message", "Hello"
        );
        LocalDateTime time = LocalDateTime.now();

        when(taskRepository.insert(any(TaskEntity.class))).thenReturn(24L);

        var result = taskManager.schedule(category, (Class<Task>) taskClass, map, time);

        assertEquals(result, 24L);

    }

    @Test
    void cancel() {

        String category = "email";
        long taskId = 24L;

        when(taskRepository.cancel(category, taskId)).thenReturn(true);

        var result = taskManager.cancel(category, taskId);

        assertTrue(result);
    }

}
