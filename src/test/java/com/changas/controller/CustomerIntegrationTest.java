package com.changas.controller;

import com.changas.controller.utils.DataLoader;
import com.changas.controller.utils.ModelTestResource;
import com.changas.dto.ApiResponse;
import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.LoginResponse;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.customer.EditCustomerRequest;
import com.changas.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerIntegrationTest {

    private final String BASE_URL = "/api/v1/customers";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DataLoader dataLoader;

    private Customer customer;

    @BeforeEach
    void setup() throws Exception {
        customer = dataLoader.createCustomer(ModelTestResource.getSignupRequest());

    }

    @AfterEach
    void teardown() {
        dataLoader.deleteAllCustomers();
    }

    @Test
    @DisplayName("Getting an overview of an existing customer returns a 200 code")
    void getCustomerOverviewTest() {
        ParameterizedTypeReference<ApiResponse<CustomerOverviewDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<CustomerOverviewDTO>> response = restTemplate.exchange(BASE_URL + "/" + customer.getId(), HttpMethod.GET, null, responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
    }

    @Test
    @DisplayName("Getting an overview of an non existing customer returns a 404 code")
    void getNonExistingCustomerTest() {
        ParameterizedTypeReference<ApiResponse<CustomerOverviewDTO>> responseType = new ParameterizedTypeReference<>() {
        };

        dataLoader.deleteAllCustomers();

        ResponseEntity<ApiResponse<CustomerOverviewDTO>> response = restTemplate.exchange(BASE_URL + "/" + customer.getId(), HttpMethod.GET, null, responseType);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
    }

    @Test
    @DisplayName("Getting posts from an existing customer returns a 200 code")
    void getCustomerPosts() {
        ParameterizedTypeReference<ApiResponse<List<ChangaOverviewDTO>>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<List<ChangaOverviewDTO>>> response = restTemplate.exchange(BASE_URL + "/" + customer.getId() + "/posts", HttpMethod.GET, null, responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Editing a customer info returns an overview of the update and a 200 code")
    void editCustomerTest() {
        ParameterizedTypeReference<ApiResponse<LoginResponse>> postResponseEntity = new ParameterizedTypeReference<>() {
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(ModelTestResource.getLoginRequest(), headers);

        ResponseEntity<ApiResponse<LoginResponse>> loginResponse = restTemplate.exchange("/api/v1/auth/login", HttpMethod.POST, requestEntity, postResponseEntity);

        if (loginResponse.getStatusCode().is2xxSuccessful()) {
            String token = Objects.requireNonNull(loginResponse.getBody()).getData().token();
            assertNotNull(token);

            ParameterizedTypeReference<ApiResponse<CustomerOverviewDTO>> editResponseEntity = new ParameterizedTypeReference<>() {
            };


            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setContentType(MediaType.APPLICATION_JSON);
            authHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            EditCustomerRequest request = new EditCustomerRequest("Pablo", "NewPhotoUrl");
            HttpEntity<EditCustomerRequest> editRequestEntity = new HttpEntity<>(request, authHeaders);

            ResponseEntity<ApiResponse<CustomerOverviewDTO>> editResponse = restTemplate.exchange(BASE_URL + "/edit", HttpMethod.PUT, editRequestEntity, editResponseEntity);

            assertEquals(HttpStatus.OK, editResponse.getStatusCode());
            assertEquals("Pablo", Objects.requireNonNull(editResponse.getBody()).getData().getName());

        } else {
            fail("Login failed with status code: " + loginResponse.getStatusCode());
        }
    }
}
