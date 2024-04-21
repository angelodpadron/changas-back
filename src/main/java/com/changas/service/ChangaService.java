package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.repository.ChangaRepository;
import com.changas.repository.HiringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChangaService {
    private final ChangaRepository changaRepository;
    private final HiringTransactionRepository hiringTransactionRepository;

    public List<ChangaOverviewDTO> getAllChangas() {
        List<ChangaOverviewDTO> overviews = new ArrayList<>();
        changaRepository.findAll().forEach(changa -> {
            ChangaOverviewDTO changaOverview = ChangaOverviewDTO
                    .builder()
                    .title(changa.getTitle())
                    .id(changa.getId())
                    .topics(changa.getTopics())
                    .description(changa.getDescription())
                    .photoUrl(changa.getPhotoUrl())
                    .customerSummary(CustomerOverviewDTO
                            .builder()
                            .id(changa.getProvider().getId())
                            .name(changa.getProvider().getName())
                            .email(changa.getProvider().getEmail())
                            .photoUrl(changa.getProvider().getPhotoUrl())
                            .build())
                    .build();

            overviews.add(changaOverview);
        });

        return overviews;
    }

    public ChangaOverviewDTO getChangaById(Long changaId) throws ChangaNotFoundException {
        Optional<Changa> optionalChanga = changaRepository.findById(changaId);

        if (optionalChanga.isPresent()) {
            Changa changa = optionalChanga.get();
            return ChangaOverviewDTO
                    .builder()
                    .title(changa.getTitle())
                    .id(changa.getId())
                    .topics(changa.getTopics())
                    .description(changa.getDescription())
                    .photoUrl(changa.getPhotoUrl())
                    .customerSummary(CustomerOverviewDTO
                            .builder()
                            .id(changa.getProvider().getId())
                            .name(changa.getProvider().getName())
                            .email(changa.getProvider().getEmail())
                            .photoUrl(changa.getProvider().getPhotoUrl())
                            .build())
                    .build();
        }

        throw new ChangaNotFoundException(changaId);

    }

    public HiringOverviewDTO hireChanga(Long changaId, Customer customer) throws ChangaNotFoundException {
        Changa changa = changaRepository.findById(changaId).orElseThrow(() -> new ChangaNotFoundException(changaId));

        HiringTransaction hiringTransaction = HiringTransaction
                .builder()
                .changa(changa)
                .customer(customer)
                .creationDate(Instant.now())
                .build();

        hiringTransactionRepository.save(hiringTransaction);

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

    public List<ChangaOverviewDTO> getChangaByTitleContainsIgnoreCase(String title) {
        List<ChangaOverviewDTO> ret = new ArrayList<>();
        changaRepository.findByTitleContainingIgnoreCase(title).forEach(changa -> {
            ChangaOverviewDTO changaOverview = ChangaOverviewDTO
                    .builder()
                    .title(changa.getTitle())
                    .id(changa.getId())
                    .topics(changa.getTopics())
                    .description(changa.getDescription())
                    .photoUrl(changa.getPhotoUrl())
                    .customerSummary(CustomerOverviewDTO
                            .builder()
                            .id(changa.getProvider().getId())
                            .name(changa.getProvider().getName())
                            .email(changa.getProvider().getEmail())
                            .photoUrl(changa.getProvider().getPhotoUrl())
                            .build())
                    .build();

            ret.add(changaOverview);
        });
        return ret;
    }
}
