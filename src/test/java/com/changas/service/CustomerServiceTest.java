package com.changas.service;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer();
    }

    @Test
    @DisplayName("Can get an overview of the hirings from a customer")
    void canGetAnOverviewOfTheHiringsFromACustomerTest() {
        HiringTransaction hiringTransaction = mock(HiringTransaction.class);
        Changa changa = mock(Changa.class);

        when(hiringTransaction.getId()).thenReturn(1L);
        when(hiringTransaction.getChanga()).thenReturn(changa);
        when(hiringTransaction.getCreationDate()).thenReturn(Instant.now());

        when(changa.getId()).thenReturn(1L);
        when(changa.getTitle()).thenReturn("Changa Name");
        when(changa.getDescription()).thenReturn("Changa Description");
        when(changa.getPhotoUrl()).thenReturn("Changa Photo Url");

        assertTrue(customerService.getHiringsFromCustomer(customer).isEmpty());

        customer.saveHiringTransaction(hiringTransaction);
        List<HiringOverviewDTO> hirings = customerService.getHiringsFromCustomer(customer);

        assertEquals(1, hirings.size());


    }

}
