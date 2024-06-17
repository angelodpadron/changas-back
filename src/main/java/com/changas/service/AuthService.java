package com.changas.service;

import com.changas.config.utils.JWTHelper;
import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.SignupRequest;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.exceptions.customer.CustomerAuthenticationException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
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
        checkIfCustomerExists(request.email());

        Customer customer = Customer
                .builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .photoUrl(request.photoUrl())
                .build();

        customerRepository.save(customer);

    }

    private void checkIfCustomerExists(String email) throws CustomerAlreadyRegisteredException {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyRegisteredException(email);
        }
    }

    public String login(LoginRequest request) throws CustomerAuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return customerRepository
                .findByEmail(request.email())
                .map(customer -> JWTHelper.generateToken(customer.getId(), customer.getEmail(), customer.getName(), customer.getPhotoUrl()))
                .orElseThrow(() -> new CustomerAuthenticationException("Failed to retrieve user on login"));
    }

    public Customer getCustomerAuthenticated() throws CustomerNotAuthenticatedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return customerRepository.findByEmail(authentication.getName()).orElseThrow(CustomerNotAuthenticatedException::new);
    }

}
