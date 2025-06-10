package com.alkmanistik.deferred_thread.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTaskRequest {
    private String category;
    private String taskClassName;
    private TaskParamsRequest taskParams;
    private LocalDateTime scheduledTime;
}
