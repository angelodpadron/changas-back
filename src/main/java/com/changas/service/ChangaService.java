package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.changa.UpdateChangaRequest;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.changa.UnauthorizedChangaEditException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.search.BadSearchRequestException;
import com.changas.mappers.ChangaMapper;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.repository.ChangaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.changas.mappers.ChangaMapper.toChangaOverviewDTO;


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
        Changa changa = changaRepository.findById(changaId).orElseThrow(() -> new ChangaNotFoundException(changaId));
        return toChangaOverviewDTO(changa);
    }

    public Set<ChangaOverviewDTO> findChangaByCriteriaHandler(Optional<String> title, Optional<Set<String>> topics) throws BadSearchRequestException {
        if (title.isPresent() && topics.isPresent()) return findChangasByTitleAndTopics(title.get(), topics.get());
        if (title.isPresent()) return findChangasByTitle(title.get());
        if (topics.isPresent()) return findChangasByTopic(topics.get());

        throw new BadSearchRequestException();
    }

    public Set<ChangaOverviewDTO> findChangasByTopic(Set<String> topics) {
        return changaRepository
                .findChangasByTopics(toLowerCaseSet(topics), topics.size()).stream()
                .map(ChangaMapper::toChangaOverviewDTO)
                .collect(Collectors.toSet());
    }

    public Set<ChangaOverviewDTO> findChangasByTitle(String title) {
        return changaRepository
                .findByTitleContainingIgnoreCase(title)
                .stream()
                .map(ChangaMapper::toChangaOverviewDTO)
                .collect(Collectors.toSet());
    }

    public Set<ChangaOverviewDTO> findChangasByTitleAndTopics(String title, Set<String> topics) {
        String titleWildCard = "%" + title + "%";
        return changaRepository
                .findByTitleAndTopics(titleWildCard, toLowerCaseSet(topics), topics.size())
                .stream()
                .map(ChangaMapper::toChangaOverviewDTO)
                .collect(Collectors.toSet());
    }

    public Optional<Changa> getChangaById(Long changaId) {
        return changaRepository.findById(changaId);
    }

    @Transactional
    public ChangaOverviewDTO createChanga(CreateChangaRequest request) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        Changa changa = new Changa(request.title(), request.description(), request.photoUrl(), request.topics(), customer);

        customer.saveChangaPost(changa);
        changaRepository.save(changa);

        return toChangaOverviewDTO(changa);
    }


    @Transactional
    public ChangaOverviewDTO updateChanga(Long changaId, UpdateChangaRequest request) throws CustomerNotAuthenticatedException, ChangaNotFoundException, UnauthorizedChangaEditException {
        Customer customer = authService.getCustomerAuthenticated();
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
        Customer customer = authService.getCustomerAuthenticated();
        Changa changa = getChangaById(changaId).orElseThrow(() -> new ChangaNotFoundException(changaId));

        changa.deactivateAs(customer);

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


}
