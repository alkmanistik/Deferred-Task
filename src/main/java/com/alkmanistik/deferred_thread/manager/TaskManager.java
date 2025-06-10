package com.alkmanistik.deferred_thread.manager;

import java.time.LocalDateTime;

public interface TaskManager {

    long schedule(String category, Class<Task> clazz, TaskParams params, LocalDateTime time);

    boolean cancel (String category, long taskId)

}
