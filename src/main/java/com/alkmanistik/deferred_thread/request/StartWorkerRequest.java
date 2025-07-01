package com.alkmanistik.deferred_thread.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Запрос создания выполнитель задач")
public class StartWorkerRequest {
    @NotBlank(message = "Category must not be blank")
    @Size(max = 50, message = "Category must be less than 50 characters")
    @Schema(description = "Категория", example = "email")
    private String category;
    @Min(value = 1, message = "Thread number must be at least 1")
    @Max(value = 50, message = "Thread number must not exceed 100")
    @Schema(description = "Количество потоков выполнения задач", example = "3")
    private int threadNumber;
    @Min(value = 1, message = "Tasks number must be at least 1")
    @Max(value = 200, message = "Tasks number must not exceed 200")
    @Schema(description = "Количество задач забираемых одновременно задач раз в 1 сек", example = "5")
    private int tasksNumber;
    @Min(value = 0, message = "Retry count must not be negative")
    @Max(value = 10, message = "Retry count must not exceed 10")
    @Schema(description = "Количество попыток на выполнение задачи", example = "3")
    private int retryCount;
    @Builder.Default
    @DecimalMin(value = "1.1", message = "Retry base must be at least 1.1")
    @DecimalMax(value = "10.0", message = "Retry base must not exceed 10.0")
    @Schema(description = "y = min(a^x, maxDelay) формула экспоненциальных попыток, где a = retryBase", example = "2.718")
    private double retryBase = Math.E; // Основание экспоненты (e ≈ 2.718 по умолчанию)

    @Builder.Default
    @Min(value = 1, message = "Max retry delay must be at least 1 minute")
    @Max(value = 1440, message = "Max retry delay must not exceed 1440 minutes (24 hours)")
    @Schema(description = "y = min(a^x, maxDelay) формула экспоненциальных попыток, где maxDelay = maxRetryDelayMinutes", example = "1440")
    private int maxRetryDelayMinutes = 1440; // 24 часа по умолчанию

    // Метод для удобного получения Duration
    @Hidden
    public Duration getMaxRetryDelay() {
        return Duration.ofMinutes(maxRetryDelayMinutes);
    }

}