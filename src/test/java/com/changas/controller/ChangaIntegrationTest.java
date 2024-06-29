package com.changas.controller;

import com.changas.controller.utils.DataLoader;
import com.changas.controller.utils.ModelTestResource;
import com.changas.dto.ApiResponse;
import com.changas.dto.changa.ChangaOverviewDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChangaIntegrationTest {

    private final String BASE_URL = "/api/v1/changas";

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

    @DisplayName("Getting all changas returns a 200 code")
    @Test
    void getAllChangasReturn200Test() {
        ParameterizedTypeReference<ApiResponse<List<ChangaOverviewDTO>>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<List<ChangaOverviewDTO>>> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}
