package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.repository.ChangaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangaService {
    private final ChangaRepository changaRepository;
    private final AuthService authService;

    public List<ChangaOverviewDTO> getAllChangas() {
        List<ChangaOverviewDTO> overviews = new ArrayList<>();
        changaRepository.findAll().forEach(changa -> overviews.add(toChangaOverviewDTO(changa)));
        return overviews;
    }

    public ChangaOverviewDTO getChangaOverviewById(Long changaId) throws ChangaNotFoundException {
        Optional<Changa> optionalChanga = changaRepository.findById(changaId);

        if (optionalChanga.isPresent()) {
            Changa changa = optionalChanga.get();
            return toChangaOverviewDTO(changa);
        }

        throw new ChangaNotFoundException(changaId);

    }

    public Set<ChangaOverviewDTO> findChangaWithTopics(Set<String> topics) {
        Set<ChangaOverviewDTO> overviews = new HashSet<>();
        changaRepository
                .findChangasByTopics(topics.stream().map(String::toLowerCase).collect(Collectors.toSet()))
                .forEach(changa -> overviews.add(toChangaOverviewDTO(changa)));
        return overviews;
    }

    public Optional<Changa> getChangaById(Long changaId) {
        return changaRepository.findById(changaId);
    }

    @Transactional
    public ChangaOverviewDTO createChanga(CreateChangaRequest request) throws CustomerNotAuthenticatedException {

        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);

        Changa changa = Changa.builder().title(request.title()).description(request.description()).photoUrl(request.photoUrl()).topics(request.topics()).provider(customer).build();

        customer.saveChangaPost(changa);
        changaRepository.save(changa);

        return toChangaOverviewDTO(changa);

    }

    private ChangaOverviewDTO toChangaOverviewDTO(Changa changa) {
        return ChangaOverviewDTO.builder()
                .title(changa.getTitle())
                .id(changa.getId())
                .topics(changa.getTopics())
                .description(changa.getDescription())
                .photoUrl(changa.getPhotoUrl())
                .customerSummary(CustomerOverviewDTO.builder()
                        .id(changa.getProvider().getId())
                        .name(changa.getProvider().getName())
                        .email(changa.getProvider().getEmail())
                        .photoUrl(changa.getProvider()
                                .getPhotoUrl())
                        .build())
                .build();
    }
}
