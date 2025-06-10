package com.alkmanistik.deferred_thread.exception;

public class TaskCancellationException extends RuntimeException {
    public TaskCancellationException(String message) {
        super(message);
    }
}