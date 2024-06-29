package com.changas.controller.utils;

import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.SignupRequest;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerAuthenticationException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.repository.ChangaRepository;
import com.changas.repository.CustomerRepository;
import com.changas.service.AuthService;
import com.changas.service.ChangaService;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final CustomerRepository customerRepository;
    private final ChangaRepository changaRepository;
    private final ChangaService changaService;
    private final AuthService authService;

    public DataLoader(CustomerRepository customerRepository, ChangaRepository changaRepository, ChangaService changaService, AuthService authService) {
        this.customerRepository = customerRepository;
        this.changaRepository = changaRepository;
        this.changaService = changaService;
        this.authService = authService;
    }

    public Customer createCustomer(SignupRequest request) throws Exception {
        authService.signup(request);
        return customerRepository.findByEmail(request.email()).orElseThrow(() -> new Exception("Failed to create test customer"));
    }

    public Changa createChanga(CreateChangaRequest request) throws CustomerAuthenticationException, CustomerNotAuthenticatedException, ChangaNotFoundException {
        LoginRequest loginRequest = ModelTestResource.getLoginRequest();
        authService.login(loginRequest);
        ChangaOverviewDTO overviewDTO = changaService.createChanga(request);

        return changaService.getChangaById(overviewDTO.getId());
    }

    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

    public void deleteAllChangas() {
        changaRepository.deleteAll();
    }
}
