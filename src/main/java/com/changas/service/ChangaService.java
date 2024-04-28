package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.customer.CustomerOverviewDTO;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.notification.HireChangaNotificationDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;

import com.changas.model.HiringTransaction;
import com.changas.model.Notification;
import com.changas.repository.ChangaRepository;
import com.changas.repository.HiringTransactionRepository;
import com.changas.repository.NotificationRepository;

import com.changas.repository.ChangaRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ChangaService {


    private final HiringTransactionRepository hiringTransactionRepository;
    private final NotificationRepository notificationRepository;
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


    public List<ChangaOverviewDTO> getChangaByTitleContainsIgnoreCase(String title) {
        List<ChangaOverviewDTO> retChangas = new ArrayList<>();
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

            retChangas.add(changaOverview);
        });
        return retChangas;
    }

    public List<HireChangaNotificationDTO> getNotificationsByChangaId(Long changaId) {

        Optional<Changa> changa = getChangaById(changaId);
        List<HireChangaNotificationDTO> retNotifications = new ArrayList<>();
        List<Notification> notifications = changaRepository.findNotificationsById(changaId);
        notifications.stream().forEach((notification) -> {
            HireChangaNotificationDTO dto = HireChangaNotificationDTO.builder()
                    .isRead(notification.getIsRead())
                    .createdAt(notification.getCreatedAt())
                    .customer(CustomerOverviewDTO
                            .builder()
                            .id(notification.getCustomer().getId())
                            .name(notification.getCustomer().getName())
                            .email(notification.getCustomer().getEmail())
                            .photoUrl(notification.getCustomer().getPhotoUrl())
                            .build())
                    .changa(ChangaOverviewDTO.builder()
                            .title(notification.getChanga().getTitle())
                            .id(notification.getChanga().getId())
                            .topics(notification.getChanga().getTopics())
                            .description(notification.getChanga().getDescription())
                            .photoUrl(notification.getChanga().getPhotoUrl())
                            .customerSummary(CustomerOverviewDTO.builder()
                                    .id(notification.getChanga().getProvider().getId())
                                    .name(notification.getChanga().getProvider().getName())
                                    .email(notification.getChanga().getProvider().getEmail())
                                    .photoUrl(notification.getChanga().getProvider().getPhotoUrl())
                                    .build())
                            .build())
                    .build();
            retNotifications.add(dto);
        });

        return retNotifications;
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
