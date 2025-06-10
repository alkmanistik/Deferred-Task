package com.alkmanistik.deferred_thread.response;

import com.alkmanistik.deferred_thread.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private Long taskId;
    private String category;
    private TaskStatus status;
    private LocalDateTime scheduledTime;
}
