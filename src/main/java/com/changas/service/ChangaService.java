package com.changas.service;

import com.changas.dto.ChangaOverviewDTO;
import com.changas.dto.CustomerOverviewDTO;
import com.changas.exceptions.ChangaNotFoundException;
import com.changas.exceptions.CustomerNotFoundException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.repository.ChangaRepository;
import com.changas.repository.CustomerRepository;
import com.changas.repository.HiringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ChangaService {
    private final ChangaRepository changaRepository;
    private final CustomerRepository customerRepository;
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

    public void hireChanga(Long changaId, Long customerId) throws ChangaNotFoundException, CustomerNotFoundException {
        Optional<Changa> optionalChanga = changaRepository.findById(changaId);

        if (optionalChanga.isEmpty()) {
            throw new ChangaNotFoundException(changaId);
        }

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException(customerId);
        }

        Changa changa = optionalChanga.get();
        Customer customer = optionalCustomer.get();
        HiringTransaction transaction = HiringTransaction
                .builder()
                .changa(changa)
                .customer(customer)
                .build();

        hiringTransactionRepository.save(transaction);

        customer.saveHiringTransaction(transaction);


    }

    public List<ChangaOverviewDTO> getChangaByTitleContainsIgnoreCase(String title) {
        List<ChangaOverviewDTO> ret = new ArrayList<ChangaOverviewDTO>();
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
