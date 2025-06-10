package com.alkmanistik.deferred_thread.task;

import java.util.HashMap;
import java.util.Map;

public class Task implements Runnable {
    private final Map<String, Object> params;

    public Task(Map<String, Object> params) {
        this.params = new HashMap<>(params);
    }

    @Override
    public void run() {
        try {
            execute(params);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Task was interrupted");
        }
    }

    protected void execute(Map<String, Object> params) throws InterruptedException {
        System.out.println("Task started with params: " + params);
        Thread.sleep(1000); // Имитация длительной работы (1 секунда)
        System.out.println("Task completed: " + params);
    }

    public Map<String, Object> getParams() {
        return new HashMap<>(params);
    }
}