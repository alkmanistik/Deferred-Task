package com.alkmanistik.deferred_thread.exception;

public class TaskSerializationException extends RuntimeException {
    public TaskSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}