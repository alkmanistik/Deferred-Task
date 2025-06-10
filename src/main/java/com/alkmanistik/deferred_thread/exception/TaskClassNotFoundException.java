package com.alkmanistik.deferred_thread.exception;

public class TaskClassNotFoundException extends RuntimeException {
    public TaskClassNotFoundException(String className) {
        super("Task class not found: " + className);
    }
}