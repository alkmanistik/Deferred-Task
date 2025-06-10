package com.alkmanistik.deferred_thread.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartWorkerRequest {
    private String category;
    private int threadNumber;
    private int tasksNumber;
    private int retryCount;
}