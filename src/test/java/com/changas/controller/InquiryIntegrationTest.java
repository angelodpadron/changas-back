package com.changas.controller;

import com.changas.controller.utils.DataLoader;
import com.changas.controller.utils.ModelTestResource;
import com.changas.dto.ApiResponse;
import com.changas.dto.question.InquiryDTO;
import com.changas.model.Changa;
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
class InquiryIntegrationTest {

    private final String BASE_URL = "/api/v1/inquiries";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DataLoader dataLoader;

    private Changa changa;

    @BeforeEach
    void setup() throws Exception {
        dataLoader.createCustomer(ModelTestResource.getSignupRequest());
        changa = dataLoader.createChanga(ModelTestResource.getCreateChangaRequest());
    }

    @AfterEach
    void teardown() {
        dataLoader.deleteAllChangas();
        dataLoader.deleteAllCustomers();
    }

    @DisplayName("Getting inquiries from a changa returns a 200 code")
    @Test
    void getInquiriesFromChangaTest() {
        ParameterizedTypeReference<ApiResponse<List<InquiryDTO>>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<List<InquiryDTO>>> response = restTemplate.exchange(BASE_URL + "/changa/" + changa.getId(), HttpMethod.GET, null, responseType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}


