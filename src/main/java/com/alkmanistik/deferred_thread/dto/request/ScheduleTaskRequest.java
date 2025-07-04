package com.alkmanistik.deferred_thread.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на добавление задачи")
public class ScheduleTaskRequest {
    @NotBlank(message = "Category must not be blank")
    @Size(max = 50, message = "Category must be less than 50 characters")
    @Schema(description = "Категория задачи", example = "email")
    private String category;
    @NotBlank(message = "Task class name must not be blank")
    @Schema(description = "Имя задачи", example = "com.alkmanistik.deferred_thread.task.EmailProcessingTask")
    private String taskClassName;
    @Schema(description = "Параметры задачи", example = """
            {
                    "email": "erik.fattakhov.02@mail.ru",
                    "message": "Hello"
            }
            """)
    private Map<String, Object> taskParams;
    @NotNull(message = "Scheduled time must not be null")
    @Schema(description = "Запланированное время", example = "2025-06-10T22:39:39.492")
    private LocalDateTime scheduledTime;
}
