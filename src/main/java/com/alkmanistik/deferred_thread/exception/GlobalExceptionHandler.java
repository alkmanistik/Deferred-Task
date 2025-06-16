package com.alkmanistik.deferred_thread.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskClassNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTaskClassNotFound(TaskClassNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TaskSerializationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTaskSerialization(TaskSerializationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TaskCancellationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleTaskCancellation(TaskCancellationException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(WorkerAlreadyExist.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleWorkerAlreadyExist(WorkerAlreadyExist ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(WorkerNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWorkerNotFound(WorkerNotFound ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(TaskParameterValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTaskParameterValidationException(TaskParameterValidationException ex) {
        return ex.getMessage();
    }

}
