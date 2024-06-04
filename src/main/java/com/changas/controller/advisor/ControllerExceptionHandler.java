package com.changas.controller.advisor;

import com.changas.dto.ApiError;
import com.changas.dto.ApiResponse;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.changa.UnauthorizedChangaEditException;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.exceptions.customer.CustomerAuthenticationException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.exceptions.review.ReviewException;
import com.changas.exceptions.review.ReviewNotFoundException;
import com.changas.exceptions.inquiry.InquiryException;
import com.changas.exceptions.search.BadSearchRequestException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({ChangaNotFoundException.class, CustomerNotFoundException.class, HiringTransactionNotFoundException.class, ReviewNotFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(asApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({CustomerAlreadyRegisteredException.class, BadSearchRequestException.class, IllegalTransactionOperationException.class, UnauthorizedChangaEditException.class, ReviewException.class, InquiryException.class})
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(asApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class, CustomerAuthenticationException.class})
    public ResponseEntity<ApiResponse<?>> handleBaCredentialsException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(asApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        List<String> detailMessages = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(asApiErrorResponse("Validation failed", detailMessages));
    }

    private ApiResponse<?> asApiErrorResponse(String message, List<String> details) {
        ApiError apiError = new ApiError(message, details);
        return ApiResponse.error(apiError);
    }

    private ApiResponse<?> asApiErrorResponse(String message) {
        return asApiErrorResponse(message, List.of());
    }


}
