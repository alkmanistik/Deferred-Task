package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.data.entity.TaskEntity;
import com.alkmanistik.deferred_thread.data.enums.TaskStatus;
import com.alkmanistik.deferred_thread.exception.TaskSerializationException;
import com.alkmanistik.deferred_thread.repository.CustomTaskRepository;
import com.alkmanistik.deferred_thread.task.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskManagerImpl implements TaskManager {

    private final CustomTaskRepository customTaskRepository;
    private final ObjectMapper objectMapper;

    @Override
    public long schedule(String category, Class<Task> clazz,
                         Map<String, Object> params, LocalDateTime time) {
        TaskEntity task = new TaskEntity();
        task.setCategory(category);
        task.setTaskClassName(clazz.getName());
        task.setTaskParamsJson(serializeParams(params));
        task.setScheduledTime(time);
        task.setStatus(TaskStatus.SCHEDULED);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        return customTaskRepository.insert(task);
    }

    @Override
    public boolean cancel(String category, long taskId) {
        return customTaskRepository.cancel(category, taskId);
    }

    private String serializeParams(Map<String, Object> params) {
        try {
            return objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new TaskSerializationException("Failed to serialize task params", e);
        }
    }

}
