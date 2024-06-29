package com.changas.controller;

import com.changas.controller.utils.DataLoader;
import com.changas.controller.utils.ModelTestResource;
import com.changas.dto.ApiResponse;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    void setup() {
        customer = dataLoader.createCustomer(ModelTestResource.getCustomer());

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
}
