package com.changas.service;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.*;
import com.changas.repository.HiringTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private AuthService authService;
    @Mock
    private HiringTransactionRepository transactionRepository;
    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setup() throws CustomerNotAuthenticatedException {
        Customer customer = new Customer();
        when(authService.getCustomerAuthenticated()).thenReturn(customer);
    }

    @Test
    @DisplayName("Can get an overview of all the transactions of a logged in customer")
    void allTransactionsFromLoggedInCustomerTest() throws CustomerNotAuthenticatedException {
        HiringTransaction transaction = mock(HiringTransaction.class);
        Changa changa = mock(Changa.class);
        Customer requester = mock(Customer.class);
        Customer provider = mock(Customer.class);

        when(changa.getId()).thenReturn(1L);
        when(requester.getId()).thenReturn(2L);
        when(provider.getId()).thenReturn(1L);
        when(transaction.getRequester()).thenReturn(requester);
        when(transaction.getProvider()).thenReturn(provider);
        when(transaction.getChanga()).thenReturn(changa);
        when(transaction.getWorkAreaDetails()).thenReturn(new WorkAreaDetails());
        when(transaction.getProviderProposal()).thenReturn(new ProviderProposal());
        when(transactionRepository.allCustomerTransactions(any())).thenReturn(new HashSet<>(List.of(transaction)));

        List<HiringOverviewDTO> hirings = customerService.getAllTransactionsFromCustomer();

        assertEquals(1, hirings.size());

    }

}
