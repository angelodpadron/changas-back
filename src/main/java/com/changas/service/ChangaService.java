package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.search.BadSearchRequestException;
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


    public List<ChangaOverviewDTO> getAllChangaOverviews() {
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

    public Set<ChangaOverviewDTO> findChangaByCriteriaHandler(Optional<String> title, Optional<Set<String>> topics) throws BadSearchRequestException {
        if (title.isPresent() && topics.isPresent()) {
            return findChangasByTitleAndTopics(title.get(), topics.get());
        }

        if (title.isPresent()) {
            return findChangasByTitle(title.get());
        }

        if (topics.isPresent()) {
            return findChangasByTopic(topics.get());
        }

        throw new BadSearchRequestException();

    }

    public Set<ChangaOverviewDTO> findChangasByTopic(Set<String> topics) {
        Set<ChangaOverviewDTO> overviews = new HashSet<>();
        changaRepository
                .findChangasByTopics(topics.stream().map(String::toLowerCase).collect(Collectors.toSet()))
                .forEach(changa -> overviews.add(toChangaOverviewDTO(changa)));
        return overviews;
    }

    public Set<ChangaOverviewDTO> findChangasByTitle(String title) {
        Set<ChangaOverviewDTO> overviews = new HashSet<>();
        changaRepository
                .findByTitleContainingIgnoreCase(title)
                .forEach(changa -> overviews.add(toChangaOverviewDTO(changa)));
        return overviews;
    }

    public Set<ChangaOverviewDTO> findChangasByTitleAndTopics(String title, Set<String> topics) {
        Set<ChangaOverviewDTO> overviews = new HashSet<>();
        String titleWildCard = "%" + title + "%";
        changaRepository
                .findByTitleAndTopics(titleWildCard, topics)
                .forEach(changa -> overviews.add(toChangaOverviewDTO(changa)));
        return overviews;
    }

    public Optional<Changa> getChangaById(Long changaId) {
        return changaRepository.findById(changaId);
    }

    @Transactional
    public ChangaOverviewDTO createChanga(CreateChangaRequest request) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        Changa changa = Changa
                .builder()
                .title(request.title())
                .description(request.description())
                .photoUrl(request.photoUrl())
                .topics(request.topics())
                .provider(customer)
                .build();

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
