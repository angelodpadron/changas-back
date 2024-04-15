package com.changas.service;

import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.SignupRequest;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
import com.changas.utils.JWTHelper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;


    private final PasswordEncoder passwordEncoder;

    public void signup(@NotNull SignupRequest request) throws CustomerAlreadyRegisteredException {
        String email = request.email();
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyRegisteredException(email);
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        customerRepository.save(Customer.builder()
                .name(request.name())
                .email(email)
                .password(hashedPassword)
                .photoUrl(request.photoUrl())
                .build()
        );

    }

    public String login(LoginRequest request) {
        // TODO: build the customer with the data provided by the authentication manager instead of querying the database

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customerLoggedIn = this.getCustomerLoggedIn().orElseThrow(() -> new RuntimeException("Failed to retrieve user on login"));
        return JWTHelper.generateToken(String.valueOf(customerLoggedIn.getId()), customerLoggedIn.getEmail(), customerLoggedIn.getName(), customerLoggedIn.getPhotoUrl());

    }

    public Optional<Customer> getCustomerLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return customerRepository.findByEmail(authentication.getName());
    }

}
