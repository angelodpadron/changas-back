package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.LoginResponse;
import com.changas.dto.auth.SignupRequest;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.exceptions.customer.CustomerAuthenticationException;
import com.changas.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) throws CustomerAlreadyRegisteredException {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) throws CustomerAuthenticationException {
        String token = authService.login(request);
        LoginResponse loginResponse = new LoginResponse(request.email(), token);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

}
