package com.alkmanistik.deferred_thread.exception;

import com.alkmanistik.deferred_thread.dto.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ValidationErrorResponse>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<ValidationErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorResponse(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errors);
    }

}
