package com.alkmanistik.deferred_thread.controller;

import com.alkmanistik.deferred_thread.model.enums.TaskStatus;
import com.alkmanistik.deferred_thread.request.ScheduleTaskRequest;
import com.alkmanistik.deferred_thread.response.TaskResponse;
import com.alkmanistik.deferred_thread.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void createTask() throws Exception {

        String request = """
                {
                    "category": "email",
                    "taskClassName": "com.alkmanistik.deferred_thread.task.EmailProcessingTask",
                    "taskParams": {
                        "email": "erik.fattakhov.02@mail.ru",
                        "message": "Hello"
                    },
                    "scheduledTime": "2025-06-20T22:39:39.492"
                }
                """;

        TaskResponse mockResponse = new TaskResponse();
        mockResponse.setTaskId(24L);
        mockResponse.setCategory("email");
        mockResponse.setStatus(TaskStatus.SCHEDULED);
        mockResponse.setScheduledTime(LocalDateTime.parse("2025-06-10T22:39:39.492"));

        when(taskService.scheduleTask(any(ScheduleTaskRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/tasks/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(24))
                .andExpect(jsonPath("$.category").value("email"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.scheduledTime").value("2025-06-10T22:39:39.492"));

    }

    @Test
    void createTaskWithValidationProblem() throws Exception {

        String request = """
                {
                    "category": "email",
                    "taskClassName": "com.alkmanistik.deferred_thread.task.EmailProcessingTask",
                    "taskParams": {
                        "email": "erik.fattakhov.02@mail.ru",
                        "message": "Hello"
                    },
                    "scheduledTime": "2025-45-20T22:39:39.492"
                }
                """;

        TaskResponse mockResponse = new TaskResponse();
        mockResponse.setTaskId(24L);
        mockResponse.setCategory("email");
        mockResponse.setStatus(TaskStatus.SCHEDULED);
        mockResponse.setScheduledTime(LocalDateTime.parse("2025-06-10T22:39:39.492"));

        when(taskService.scheduleTask(any(ScheduleTaskRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/tasks/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is4xxClientError());

    }

    @Test
    void cancelTask() throws Exception {

        Long taskId = 24L;
        String request = """
                {
                    "category": "email"
                }
                """;

        mockMvc.perform(patch("/api/v1/tasks/cancel/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)  // Указываем тип содержимого
                .content(request))
                .andExpect(status().isNoContent());

    }

    @Test
    void cancelTaskWithValidationProblem() throws Exception {

        Long taskId = 24L;
        String request = """
                {
                    "category": ""
                }
                """;

        mockMvc.perform(patch("/api/v1/tasks/cancel/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)  // Указываем тип содержимого
                        .content(request))
                .andExpect(status().is4xxClientError());

    }

}
