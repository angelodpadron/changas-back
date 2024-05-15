package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.changa.UpdateChangaRequest;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.changa.UnauthorizedChangaEditException;
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
        if (title.isPresent() && topics.isPresent()) return findChangasByTitleAndTopics(title.get(), topics.get());
        if (title.isPresent()) return findChangasByTitle(title.get());
        if (topics.isPresent()) return findChangasByTopic(topics.get());

        throw new BadSearchRequestException();
    }

    public Set<ChangaOverviewDTO> findChangasByTopic(Set<String> topics) {
        Set<ChangaOverviewDTO> overviews = new HashSet<>();
        changaRepository
                .findChangasByTopics(toLowerCaseSet(topics), topics.size())
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
                .findByTitleAndTopics(titleWildCard, toLowerCaseSet(topics), topics.size())
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
                .available(true)
                .build();

        customer.saveChangaPost(changa);
        changaRepository.save(changa);

        return toChangaOverviewDTO(changa);
    }

    @Transactional
    public ChangaOverviewDTO updateChanga(Long changaId, UpdateChangaRequest request) throws CustomerNotAuthenticatedException, ChangaNotFoundException, UnauthorizedChangaEditException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        Changa changa = getChangaById(changaId).orElseThrow(() -> new ChangaNotFoundException(changaId));

        checkIfCanEdit(customer, changa);

        request.getTitle().ifPresent(changa::setTitle);
        request.getDescription().ifPresent(changa::setDescription);
        request.getPhotoUrl().ifPresent(changa::setPhotoUrl);
        request.getTopics().ifPresent(changa::setTopics);

        changaRepository.save(changa);

        return toChangaOverviewDTO(changa);
    }

    public ChangaOverviewDTO deactivateChanga(Long changaId) throws CustomerNotAuthenticatedException, ChangaNotFoundException, UnauthorizedChangaEditException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        Changa changa = getChangaById(changaId).orElseThrow(() -> new ChangaNotFoundException(changaId));

        checkIfCanEdit(customer, changa);

        changa.setAvailable(false);
        changaRepository.save(changa);

        return toChangaOverviewDTO(changa);
    }

    private void checkIfCanEdit(Customer customer, Changa changa) throws UnauthorizedChangaEditException {
        if (!customer.getId().equals(changa.getProvider().getId())) {
            throw new UnauthorizedChangaEditException();
        }
    }

    private Set<String> toLowerCaseSet(Set<String> set) {
        return set.stream().map(String::toLowerCase).collect(Collectors.toSet());
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
                .available(changa.getAvailable())
                .build();
    }



}
