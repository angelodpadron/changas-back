package com.changas.service;


import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.customer.EditCustomerRequest;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private final HashSet<Changa> changas = new HashSet<>();

    @Mock
    private AuthService authService;

    private final Customer customer = new Customer(1L,"pepe","pepe@email.com",
            "password","photo_url",changas);

    @DisplayName("Get non-existent customer")
    @Test
    public void getNonexistentCustomerTest(){
        assertThrows(CustomerNotFoundException.class,() -> customerService.getCustomerOverview(customer.getId()));

    }

    @DisplayName("Edit customer name")
    @Test
    public void editCustomerNameTest() throws CustomerNotAuthenticatedException {
        when(authService.getCustomerAuthenticated()).thenReturn(customer);
        EditCustomerRequest editName = new EditCustomerRequest("carlos","photo_url");

        CustomerOverviewDTO customerOverviewDTO = customerService.editCustomer(editName);

        assertFalse(Boolean.parseBoolean(customer.getName()),customerOverviewDTO.getName());
    }

    @DisplayName("Get customer Posts")
    @Test
    public void getCustomerPosts() throws CustomerNotFoundException {

        customer.setPosts(changas);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        List<ChangaOverviewDTO> posts = customerService.getCustomerPosts(customer.getId());

        assertEquals(customer.getPosts().size(), posts.size());
    }

}
