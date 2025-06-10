package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.task.Task;

import java.time.LocalDateTime;
import java.util.Map;

public interface TaskManager {

    long schedule(String category, Class<Task> clazz, Map<String, Object> params, LocalDateTime time);

    boolean cancel(String category, long taskId);
}
