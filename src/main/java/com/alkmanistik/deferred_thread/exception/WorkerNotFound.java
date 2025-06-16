package com.alkmanistik.deferred_thread.exception;

public class WorkerNotFound extends RuntimeException {
    public WorkerNotFound(String message) {
        super(message);
    }
}
