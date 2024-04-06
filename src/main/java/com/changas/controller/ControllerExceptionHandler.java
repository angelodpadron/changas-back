package com.changas.controller;

import com.changas.exceptions.ChangaNotFoundException;
import com.changas.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.ExecutionException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<String> handleInterruptedException() {
        Thread.currentThread().interrupt();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Operation was interrupted.");
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<String> handleExecutionException(ExecutionException e) {
        Throwable cause = e.getCause();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error during execution: " + cause.getMessage());
    }

    @ExceptionHandler({ChangaNotFoundException.class, CustomerNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(ChangaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
