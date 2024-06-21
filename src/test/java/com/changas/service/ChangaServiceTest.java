package com.changas.service;

import com.changas.dto.area.Geometry;
import com.changas.dto.area.ServiceAreaRequest;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.changa.UpdateChangaRequest;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.changa.UnauthorizedChangaEditException;
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
import java.util.Optional;
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
    @Mock
    ServiceAreaService serviceAreaService;

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
        CreateChangaRequest createChangaRequest = generateCreateChangaRequest();

        when(authService.getCustomerAuthenticated()).thenReturn(testCustomer);

        changaService.createChanga(createChangaRequest);

        verify(changaRepository).save(any(Changa.class));
        assertFalse(testCustomer.getPosts().isEmpty());
    }

    @Test
    @DisplayName("A successful changa creation returns an overview of it")
    void getChangaOverviewAfterCreationSuccessTest() throws Exception {
        CreateChangaRequest createChangaRequest = generateCreateChangaRequest();
        when(authService.getCustomerAuthenticated()).thenReturn(testCustomer);

        ChangaOverviewDTO changaOverviewDTO = changaService.createChanga(createChangaRequest);

        assertNotNull(changaOverviewDTO);
    }

    @Test
    @DisplayName("Attempting to create a changa without an authenticated user throws an exception")
    void createChangaWithoutAuthenticatedUser() throws CustomerNotAuthenticatedException {
        CreateChangaRequest createChangaRequest = generateCreateChangaRequest();
        when(authService.getCustomerAuthenticated()).thenThrow(CustomerNotAuthenticatedException.class);

        assertThrows(CustomerNotAuthenticatedException.class, () -> changaService.createChanga(createChangaRequest));

    }

    @Test
    @DisplayName("A provider can update its service information")
    void updateChangaTest() throws CustomerNotAuthenticatedException, UnauthorizedChangaEditException, ChangaNotFoundException {
        when(authService.getCustomerAuthenticated()).thenReturn(testCustomer);
        ServiceArea area = mock(ServiceArea.class);

        Changa changa = mock(Changa.class);
        when(changa.getId()).thenReturn(1L);
        when(changa.getServiceArea()).thenReturn(area);
        when(changa.getProvider()).thenReturn(testCustomer);

        when(changaRepository.findById(any())).thenReturn(Optional.of(changa));

        UpdateChangaRequest request = generateUpdateChangaRequest();

        changaService.updateChanga(changa.getId(), request);

        request.getTitle().ifPresent(title -> verify(changa).setTitle(title));
        request.getDescription().ifPresent(description -> verify(changa).setDescription(description));
        request.getPhotoUrl().ifPresent(photoUrl -> verify(changa).setPhotoUrl(photoUrl));
        request.getTopics().ifPresent(topics -> verify(changa).setTopics(topics));
        request.getServiceAreaRequest().ifPresent(update -> verify(serviceAreaService).updateServiceArea(area, update));

        verify(changaRepository).save(changa);

    }

    private Customer createTestCustomer() {
        return Customer.builder().id(1L).name("Pepe").email("pepe@email.com").password("password").photoUrl("photoUrl").posts(new HashSet<>()).build();
    }

    private CreateChangaRequest generateCreateChangaRequest() {
        ServiceAreaRequest serviceAreaRequest = new ServiceAreaRequest("Address", new Geometry("Point", new Double[]{-58.2912458, -34.7955703}));
        return new CreateChangaRequest("Changa Title", "Description", "https://image.org/image.jpg", new HashSet<>(), serviceAreaRequest);
    }

    private UpdateChangaRequest generateUpdateChangaRequest() {
        UpdateChangaRequest request = new UpdateChangaRequest();
        request.setTitle("New Title");
        request.setDescription("New Description");
        request.setTopics(Set.of());
        request.setPhotoUrl("New Photo URL");
        request.setServiceAreaRequest(new ServiceAreaRequest("New Address", new Geometry("Point", new Double[]{-58.2912458, -34.7955703})));

        return request;
    }

}
