package com.alkmanistik.deferred_thread.exception;

public class WorkerAlreadyExist extends RuntimeException {
    public WorkerAlreadyExist(String message) {
        super(message);
    }
}
