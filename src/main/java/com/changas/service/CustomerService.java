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

    public List<HiringOverviewDTO> getHiringsFromCustomer(Customer customer) {
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
