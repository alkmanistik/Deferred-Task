package com.alkmanistik.deferred_thread.service;

import com.alkmanistik.deferred_thread.task.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TaskManagerImpl implements TaskManager {


    @Override
    public long schedule(String category, Class<Task> clazz, Map<String, Object> params, LocalDateTime time) {
        return 0;
    }

    @Override
    public boolean cancel(String category, long taskId) {
        return false;
    }

}
