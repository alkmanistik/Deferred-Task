package com.alkmanistik.deferred_thread.controller;

import com.alkmanistik.deferred_thread.service.WorkerManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkerController.class)
public class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkerManager workerManager;

    @Test
    void startWorker() throws Exception {

        String request = """
                {
                    "category": "email",
                    "threadNumber": 3,
                    "tasksNumber": 200,
                    "retryCount": 3,
                    "retryBase": 4.0
                }
                """;

        String response = String.format("Worker for category '%s' started with %d threads",
                "email", 3);

        mockMvc.perform(post("/api/v1/workers/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

    }

    @Test
    void startWorkerWithValidationProblem() throws Exception {

        String request = """
                {
                    "category": "email",
                    "threadNumber": 10000,
                    "tasksNumber": 200,
                    "retryCount": 3,
                    "retryBase": 4.0
                }
                """;

        String response = String.format("Worker for category '%s' started with %d threads",
                "email", 3);

        mockMvc.perform(post("/api/v1/workers/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is4xxClientError());

    }

    @Test
    void stopWorker() throws Exception {

        String category = "email";

        String response = String.format("Worker for category '%s' stopped", "email");

        mockMvc.perform(post("/api/v1/workers/stop/{category}", category))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    void stopWorkerWithValidationProblem() throws Exception {

        String category = "";

        mockMvc.perform(post("/api/v1/workers/stop/{category}", category))
                .andExpect(status().is4xxClientError());
    }

}
