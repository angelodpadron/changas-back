package com.changas.controller;

import com.changas.dto.ApiError;
import com.changas.dto.ApiResponse;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({ChangaNotFoundException.class, CustomerNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(ChangaNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(asApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(CustomerAlreadyRegisteredException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomerAlreadyRegisteredException(CustomerAlreadyRegisteredException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(asApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleBaCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(asApiErrorResponse(e.getMessage()));
    }

    private ApiResponse<?> asApiErrorResponse(String message, String... details) {
        ApiError apiError = new ApiError(message, Arrays.stream(details).toList());
        return ApiResponse.error(apiError);
    }

}
