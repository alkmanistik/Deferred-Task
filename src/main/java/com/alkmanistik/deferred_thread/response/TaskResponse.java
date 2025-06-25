package com.alkmanistik.deferred_thread.response;

import com.alkmanistik.deferred_thread.data.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные задачи")
public class TaskResponse {
    @Schema(description = "Уникальный идентификатор задачи", example = "24", accessMode = Schema.AccessMode.READ_ONLY)
    private Long taskId;
    @Schema(description = "Категория задачи", example = "email", accessMode = Schema.AccessMode.READ_ONLY)
    private String category;
    @Schema(description = "Статус задачи", example = "SCHEDULED", accessMode = Schema.AccessMode.READ_ONLY)
    private TaskStatus status;
    @Schema(description = "Запланированное время выполнения", example = "2025-06-10T22:39:39.492", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime scheduledTime;
}
