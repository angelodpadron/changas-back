package com.changas.controller.advisor;

import com.changas.dto.ApiError;
import com.changas.dto.ApiResponse;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.changa.UnauthorizedChangaEditException;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.exceptions.customer.CustomerAuthenticationException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.exceptions.inquiry.InquiryException;
import com.changas.exceptions.search.BadSearchRequestException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
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
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(asApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({CustomerAlreadyRegisteredException.class, BadSearchRequestException.class, IllegalTransactionOperationException.class, UnauthorizedChangaEditException.class, InquiryException.class})
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(asApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class, CustomerAuthenticationException.class})
    public ResponseEntity<ApiResponse<?>> handleBaCredentialsException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(asApiErrorResponse(e.getMessage()));
    }

    private ApiResponse<?> asApiErrorResponse(String message, String... details) {
        ApiError apiError = new ApiError(message, Arrays.stream(details).toList());
        return ApiResponse.error(apiError);
    }

}
