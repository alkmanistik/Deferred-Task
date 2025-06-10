package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.entity.TaskEntity;
import com.alkmanistik.deferred_thread.entity.enums.TaskStatus;
import com.alkmanistik.deferred_thread.exception.TaskSerializationException;
import com.alkmanistik.deferred_thread.repository.TaskRepository;
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

    private final TaskRepository taskRepository;
    private final WorkerManager workerManager;
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

        TaskEntity savedTask = taskRepository.save(task);

        return savedTask.getId();
    }

    @Override
    public boolean cancel(String category, long taskId) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    if (task.getStatus() == TaskStatus.SCHEDULED) {
                        task.setStatus(TaskStatus.CANCELLED);
                        task.setUpdatedAt(LocalDateTime.now());
                        taskRepository.save(task);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    private String serializeParams(Map<String, Object> params) {
        try {
            return objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new TaskSerializationException("Failed to serialize task params", e);
        }
    }

}
