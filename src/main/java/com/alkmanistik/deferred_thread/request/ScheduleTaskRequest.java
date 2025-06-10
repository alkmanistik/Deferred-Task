package com.alkmanistik.deferred_thread.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTaskRequest {
    private String category;
    private String taskClassName;
    private Map<String, Object> taskParams;
    private LocalDateTime scheduledTime;
}
