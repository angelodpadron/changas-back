package com.changas.service;

import com.changas.dto.HiringOverviewDTO;
import com.changas.dto.SignupRequest;
import com.changas.exceptions.CustomerAlreadyRegisteredException;
import com.changas.exceptions.CustomerNotFoundException;
import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
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

    public List<HiringOverviewDTO> getHiringsFromCustomer(Long customerId) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException(customerId);
        }

        Customer customer = optionalCustomer.get();

        List<HiringOverviewDTO> hirings = new ArrayList<>();

        customer.getHirings().forEach(hiringTransaction -> {
            HiringOverviewDTO hiringOverview = HiringOverviewDTO
                    .builder()
                    .hiringId(hiringTransaction.getId())
                    .changaId(hiringTransaction.getChanga().getId())
                    .changaTitle(hiringTransaction.getChanga().getTitle())
                    .changaDescription(hiringTransaction.getChanga().getDescription())
                    .changaPhotoUrl(hiringTransaction.getChanga().getPhotoUrl())
                    .creationDate(Instant.now())
                    .build();

            hirings.add(hiringOverview);

        });

        return hirings;

    }

}
