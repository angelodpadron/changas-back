package com.changas.service;

import com.changas.dto.hiring.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.WorkAreaDetailsDTO;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.status.TransactionStatus;
import com.changas.repository.HiringTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private final WorkAreaDetailsDTO workAreaDetails = new WorkAreaDetailsDTO("photo_url", "work_area_description");

    @BeforeEach
    void setUp() {
        this.customer = mock(Customer.class);
        this.provider = mock(Customer.class);
        this.changa = mock(Changa.class);
    }

    @Test
    @DisplayName("A hiring request generates a hiring transaction overview with an awaiting provider confirmation status")
    void generateHiringTransactionOverview() throws HiringOwnChangaException, CustomerNotAuthenticatedException, ChangaNotFoundException {
        when(customer.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(provider);  // The changa is not created by the customer interested in hiring
        when(authService.getCustomerLoggedIn()).thenReturn(Optional.of(customer));
        when(changaService.getChangaById(changa.getId())).thenReturn(Optional.of(changa));

        HireChangaRequest hireChangaRequest = new HireChangaRequest(changa.getId(), this.workAreaDetails);
        HiringOverviewDTO hiringOverviewDTO = hiringTransactionService.hireChanga(hireChangaRequest);

        assertNotNull(hiringOverviewDTO);
        assertEquals(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION, hiringOverviewDTO.getStatus());

    }

    @Test
    @DisplayName("Hiring an owned changa throws an exception")
    void hiringOwnChangaExceptionTest() {
        when(customer.getId()).thenReturn(1L);
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(customer);  // The changa is created by the customer interested in hiring
        when(authService.getCustomerLoggedIn()).thenReturn(Optional.of(customer));
        when(changaService.getChangaById(changa.getId())).thenReturn(Optional.of(changa));

        HireChangaRequest hireChangaRequest = new HireChangaRequest(changa.getId(), this.workAreaDetails);

        assertThrows(HiringOwnChangaException.class, () -> hiringTransactionService.hireChanga(hireChangaRequest));
    }

    @Test
    @DisplayName("Hiring a unavailable changa throws an exception")
    void hiringAnUnavailableChangaTest() {
        when(customer.getId()).thenReturn(1L);
        when(authService.getCustomerLoggedIn()).thenReturn(Optional.of(customer));
        when(changaService.getChangaById(any())).thenReturn(Optional.empty());

        HireChangaRequest hireChangaRequest = new HireChangaRequest(1L, this.workAreaDetails);

        assertThrows(ChangaNotFoundException.class, () -> hiringTransactionService.hireChanga(hireChangaRequest));
    }

    @Test
    @DisplayName("Hiring a changa without being authenticated throws an exception")
    void hiringAChangaWithoutAuthTest() {
        when(authService.getCustomerLoggedIn()).thenReturn(Optional.empty());

        HireChangaRequest hireChangaRequest = new HireChangaRequest(1L, this.workAreaDetails);

        assertThrows(CustomerNotAuthenticatedException.class, () -> hiringTransactionService.hireChanga(hireChangaRequest));
    }

}
