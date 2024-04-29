package com.changas.service;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer();
        when(authService.getCustomerLoggedIn()).thenReturn(Optional.ofNullable(customer));
    }

    @Test
    @DisplayName("Can get an overview of the transactions from a customer")
    void canGetAnOverviewOfTheHiringsFromACustomerTest() throws CustomerNotAuthenticatedException {
        HiringTransaction hiringTransaction = mock(HiringTransaction.class);
        Changa changa = mock(Changa.class);

        when(hiringTransaction.getId()).thenReturn(1L);
        when(hiringTransaction.getChanga()).thenReturn(changa);
        when(hiringTransaction.getCreationDate()).thenReturn(Instant.now());
        when(hiringTransaction.getRequester()).thenReturn(customer);
        when(hiringTransaction.getProvider()).thenReturn(new Customer());

        when(changa.getId()).thenReturn(1L);
        when(changa.getTitle()).thenReturn("Changa Name");
        when(changa.getDescription()).thenReturn("Changa Description");
        when(changa.getPhotoUrl()).thenReturn("Changa Photo Url");

        assertTrue(customerService.getAllTransactionsFromCustomer().isEmpty());

        customer.saveHiringTransaction(hiringTransaction);
        List<HiringOverviewDTO> hirings = customerService.getAllTransactionsFromCustomer();

        assertEquals(1, hirings.size());


    }

}
