package com.alkmanistik.deferred_thread.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Builder
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
    @Builder.Default
    @DecimalMin(value = "1.1", message = "Retry base must be at least 1.1")
    @DecimalMax(value = "10.0", message = "Retry base must not exceed 10.0")
    private double retryBase = Math.E; // Основание экспоненты (e ≈ 2.718 по умолчанию)

    @Builder.Default
    @Min(value = 1, message = "Max retry delay must be at least 1 minute")
    @Max(value = 1440, message = "Max retry delay must not exceed 1440 minutes (24 hours)")
    private int maxRetryDelayMinutes = 1440; // 24 часа по умолчанию

    // Метод для удобного получения Duration
    public Duration getMaxRetryDelay() {
        return Duration.ofMinutes(maxRetryDelayMinutes);
    }

}