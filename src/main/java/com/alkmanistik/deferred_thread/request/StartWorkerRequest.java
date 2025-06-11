package com.alkmanistik.deferred_thread.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartWorkerRequest {
    @NotBlank(message = "Category must not be blank")
    @Size(max = 50, message = "Category must be less than 50 characters")
    private String category;
    @Min(value = 1, message = "Thread number must be at least 1")
    @Max(value = 50, message = "Thread number must not exceed 100")
    private int threadNumber;
    @Min(value = 1, message = "Tasks number must be at least 1")
    @Max(value = 1000, message = "Tasks number must not exceed 1000")
    private int tasksNumber;
    @Min(value = 0, message = "Retry count must not be negative")
    @Max(value = 10, message = "Retry count must not exceed 10")
    private int retryCount;
}