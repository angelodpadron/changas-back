package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.LoginResponse;
import com.changas.dto.auth.SignupRequest;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SIGNUP_ENDPOINT = "/api/v1/auth/signup";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";


    // SIGNUP

    @Test
    @DisplayName("A correct signup request returns a 201 response")
    void signupSuccess() throws Exception {
        SignupRequest request = new SignupRequest("pepe@email.com", "https://images.com/pepe.jpg", "Test User", "password");
        String requestBody = toJsonString(request);

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        verify(authService).signup(any(SignupRequest.class));
    }

    @Test
    @DisplayName("Attempting to signup with an already taken email returns a 400 response")
    void signupFailure() throws Exception{
        SignupRequest request = new SignupRequest("pepe@email.com", "https://images.com/pepe.jpg", "Test User", "password");
        String requestBody = toJsonString(request);

        doThrow(new CustomerAlreadyRegisteredException(request.email())).when(authService).signup(any(SignupRequest.class));

        mockMvc.perform(post(SIGNUP_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(authService).signup(any(SignupRequest.class));
    }

    // LOGIN

    @Test
    @DisplayName("A correct login request returns a 200 response with an api response body")
    void loginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("pepe@email.com", "password");
        String token = "fake-jwt-token";
        LoginResponse loginResponse = new LoginResponse(loginRequest.email(), token);
        ApiResponse<LoginResponse> apiResponse = ApiResponse.success(loginResponse);

        when(authService.login(any(LoginRequest.class))).thenReturn(token);

        String requestBody = toJsonString(loginRequest);
        String expectedResponse = new ObjectMapper().writeValueAsString(apiResponse);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("Attempting to login with bad credentials returns a 401 response")
    void loginFailure() throws Exception {
        LoginRequest loginRequest = new LoginRequest("pepe@email.com", "wrongPassword");
        String requestBody = toJsonString(loginRequest);

        when(authService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("Authentication failed"));

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    private String toJsonString(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

}
