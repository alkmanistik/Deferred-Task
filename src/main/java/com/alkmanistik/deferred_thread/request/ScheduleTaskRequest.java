package com.alkmanistik.deferred_thread.request;


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
public class ScheduleTaskRequest {
    @NotBlank(message = "Category must not be blank")
    @Size(max = 50, message = "Category must be less than 50 characters")
    private String category;
    @NotBlank(message = "Task class name must not be blank")
    private String taskClassName;
    private Map<String, Object> taskParams;
    @NotNull(message = "Scheduled time must not be null")
    private LocalDateTime scheduledTime;
}
