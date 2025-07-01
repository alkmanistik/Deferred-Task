package com.alkmanistik.deferred_thread.data.entity;

import com.alkmanistik.deferred_thread.data.enums.TaskStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String taskClassName;
    private String taskParamsJson;
    private LocalDateTime scheduledTime;
    private TaskStatus status;
    private int retryCount = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}