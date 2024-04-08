package com.changas.service;

import com.changas.dto.HiringOverviewDTO;
import com.changas.exceptions.CustomerNotFoundException;
import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

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
