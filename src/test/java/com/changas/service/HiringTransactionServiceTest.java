package com.changas.service;

import com.changas.dto.changa.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.repository.HiringTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class HiringTransactionServiceTest {

    @Mock
    HiringTransactionRepository hiringTransactionRepository;
    @Mock
    AuthService authService;
    @Mock
    ChangaService changaService;

    @InjectMocks
    private HiringTransactionService hiringTransactionService;

    private Customer customer;
    private Customer provider;
    private Changa changa;

    @BeforeEach
    void setUp() {
        this.customer = mock(Customer.class);
        this.provider = mock(Customer.class);
        this.changa = mock(Changa.class);
    }

    @Test
    @DisplayName("A hiring request generates a hiring transaction and saves it to the customer")
    void saveHiringTransactionInTheCustomerTest() throws HiringOwnChangaException, CustomerNotAuthenticatedException, ChangaNotFoundException {
        when(customer.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);

        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(provider);  // The changa is not created by the customer interested in hiring

        when(authService.getCustomerLoggedIn()).thenReturn(Optional.of(customer));
        when(changaService.getChangaById(changa.getId())).thenReturn(Optional.of(changa));

        HireChangaRequest hireChangaRequest = new HireChangaRequest(changa.getId(), "Work Details", "Work Area Photo URL");

        hiringTransactionService.hireChanga(hireChangaRequest);

        verify(customer).saveHiringTransaction(any());

    }

    @Test
    @DisplayName("A hiring request generates a hiring transaction overview")
    void generateHiringTransactionOverview() throws HiringOwnChangaException, CustomerNotAuthenticatedException, ChangaNotFoundException {
        when(customer.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);

        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(provider);  // The changa is not created by the customer interested in hiring

        when(authService.getCustomerLoggedIn()).thenReturn(Optional.of(customer));
        when(changaService.getChangaById(changa.getId())).thenReturn(Optional.of(changa));

        HireChangaRequest hireChangaRequest = new HireChangaRequest(changa.getId(), "Work Details", "Work Area Photo URL");

        HiringOverviewDTO hiringOverviewDTO = hiringTransactionService.hireChanga(hireChangaRequest);

        assertNotNull(hiringOverviewDTO);

    }

    @Test
    @DisplayName("Hiring an owned changa throws an exception")
    void hiringOwnChangaExceptionTest() {
        when(customer.getId()).thenReturn(1L);

        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(customer);  // The changa is created by the customer interested in hiring

        when(authService.getCustomerLoggedIn()).thenReturn(Optional.of(customer));
        when(changaService.getChangaById(changa.getId())).thenReturn(Optional.of(changa));

        HireChangaRequest hireChangaRequest = new HireChangaRequest(changa.getId(), "Work Details", "Work Area Photo URL");

        assertThrows(HiringOwnChangaException.class, () -> hiringTransactionService.hireChanga(hireChangaRequest));
    }

    @Test
    @DisplayName("Hiring a unavailable changa throws an exception")
    void hiringAnUnavailableChangaTest() {
        when(customer.getId()).thenReturn(1L);

        when(authService.getCustomerLoggedIn()).thenReturn(Optional.of(customer));
        when(changaService.getChangaById(any())).thenReturn(Optional.empty());

        HireChangaRequest hireChangaRequest = new HireChangaRequest(1L, "Work Details", "Work Area Photo URL");

        assertThrows(ChangaNotFoundException.class, () -> hiringTransactionService.hireChanga(hireChangaRequest));
    }

    @Test
    @DisplayName("Hiring a changa without beign authenticated throws an exception")
    void hiringAChangaWithoutAuthTest() {
        when(authService.getCustomerLoggedIn()).thenReturn(Optional.empty());

        HireChangaRequest hireChangaRequest = new HireChangaRequest(1L, "Work Details", "Work Area Photo URL");

        assertThrows(CustomerNotAuthenticatedException.class, () -> hiringTransactionService.hireChanga(hireChangaRequest));
    }

}