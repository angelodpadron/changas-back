package com.changas.service;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.repository.HiringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HiringTransactionService {
    private final HiringTransactionRepository repository;

    public HiringOverviewDTO hireChanga(Changa changa, Customer customer) {
        HiringTransaction hiringTransaction = HiringTransaction
                .builder()
                .changa(changa)
                .customer(customer)
                .creationDate(Instant.now())
                .build();

        repository.save(hiringTransaction);

        customer.saveHiringTransaction(hiringTransaction);

        return HiringOverviewDTO
                .builder()
                .hiringId(hiringTransaction.getId())
                .changaId(changa.getId())
                .changaPhotoUrl(changa.getPhotoUrl())
                .changaDescription(changa.getDescription())
                .changaTitle(changa.getTitle())
                .creationDate(hiringTransaction.getCreationDate())
                .build();

    }

}
