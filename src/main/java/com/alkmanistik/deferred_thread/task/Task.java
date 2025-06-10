package com.alkmanistik.deferred_thread.task;

import java.util.Map;

public class Task {

    private final Map<String, Object> params;

    public Task(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}