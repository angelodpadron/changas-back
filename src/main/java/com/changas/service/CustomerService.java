package com.changas.service;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    public List<HiringOverviewDTO> getTransactionsFromCustomer(Customer customer) {
        List<HiringOverviewDTO> hirings = new ArrayList<>();

        customer.getTransactions().forEach(hiringTransaction -> {
            HiringOverviewDTO hiringOverview = HiringOverviewDTO
                    .builder()
                    .hiringId(hiringTransaction.getId())
                    .changaId(hiringTransaction.getChanga().getId())
                    .customerId(hiringTransaction.getRequester().getId())
                    .providerId(hiringTransaction.getProvider().getId())
                    .changaTitle(hiringTransaction.getChanga().getTitle())
                    .changaDescription(hiringTransaction.getChanga().getDescription())
                    .changaPhotoUrl(hiringTransaction.getChanga().getPhotoUrl())
                    .creationDate(Instant.now())
                    .status(hiringTransaction.getStatus())
                    .build();

            hirings.add(hiringOverview);

        });

        return hirings;

    }

}
