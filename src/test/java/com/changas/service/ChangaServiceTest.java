package com.changas.service;

import com.changas.dto.area.ServiceAreaRequest;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.ServiceArea;
import com.changas.repository.ChangaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ChangaServiceTest {

    @Mock
    private ChangaRepository changaRepository;
    @Mock
    AuthService authService;

    @InjectMocks
    private ChangaService changaService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = createTestCustomer();
    }


    @Test
    @DisplayName("Search a changa with topics returns a set of changas that includes those topics")
    void findChangasByTopicsTest() {
        Changa changa1 = mock(Changa.class);
        Changa changa2 = mock(Changa.class);

        ServiceArea serviceArea = mock(ServiceArea.class);


        when(changa1.getProvider()).thenReturn(testCustomer);
        when(changa2.getProvider()).thenReturn(testCustomer);
        when(changa1.getServiceArea()).thenReturn(serviceArea);
        when(changa2.getServiceArea()).thenReturn(serviceArea);

        when(changaRepository.findChangasByTopics(any(), any(Integer.class))).thenReturn(new HashSet<>(List.of(changa1, changa2)));

        Set<ChangaOverviewDTO> filtered = changaService.findChangasByTopic(new HashSet<>(List.of("Plumbing", "Wielding")));

        assertFalse(filtered.isEmpty());
        assertEquals(2, filtered.size());
    }

    @Test
    @DisplayName("A created changa gets saved into the customer's posts collection")
    void savingCreatedChangaOnCustomerTest() throws Exception {
        CreateChangaRequest createChangaRequest = createChangaRequest();

        when(authService.getCustomerAuthenticated()).thenReturn(testCustomer);

        changaService.createChanga(createChangaRequest);

        verify(changaRepository).save(any(Changa.class));
        assertFalse(testCustomer.getPosts().isEmpty());
    }

    @Test
    @DisplayName("A successful changa creation returns an overview of it")
    void getChangaOverviewAfterCreationSuccessTest() throws Exception {
        CreateChangaRequest createChangaRequest = createChangaRequest();
        when(authService.getCustomerAuthenticated()).thenReturn(testCustomer);

        ChangaOverviewDTO changaOverviewDTO = changaService.createChanga(createChangaRequest);

        assertNotNull(changaOverviewDTO);
    }

    @Test
    @DisplayName("Attempting to create a changa without an authenticated user throws an exception")
    void createChangaWithoutAuthenticatedUser() throws CustomerNotAuthenticatedException {
        CreateChangaRequest createChangaRequest = createChangaRequest();
        when(authService.getCustomerAuthenticated()).thenThrow(CustomerNotAuthenticatedException.class);

        assertThrows(CustomerNotAuthenticatedException.class, () -> changaService.createChanga(createChangaRequest));

    }

    private Customer createTestCustomer() {
        return Customer.builder().name("Pepe").email("pepe@email.com").password("password").photoUrl("photoUrl").posts(new HashSet<>()).build();
    }

    private CreateChangaRequest createChangaRequest() {
        ServiceAreaRequest serviceAreaRequest = new ServiceAreaRequest("Address", new double[]{-58.2912458, -34.7955703});
        return new CreateChangaRequest("Changa Title", "Description", "https://image.org/image.jpg", new HashSet<>(), serviceAreaRequest);
    }

}
