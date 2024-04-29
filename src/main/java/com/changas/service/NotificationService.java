package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.notification.HireChangaNotificationDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.Notification;
import com.changas.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ChangaService changaService;

    @Transactional
    public void createNotification(Long changaId, Customer customer) throws ChangaNotFoundException {
        Changa changa = changaService.getChangaById(changaId).orElseThrow(() -> new ChangaNotFoundException(changaId));

        Notification notification = Notification
                .builder()
                .customer(customer)
                .createdAt(Instant.now())
                .isRead(false)
                .changa(changa)
                .build();

        notificationRepository.save(notification);
    }

    public List<HireChangaNotificationDTO> getAllNotifications() {
        List<HireChangaNotificationDTO> retNotification = new ArrayList<>();
        notificationRepository.findAll().forEach(notification ->
                retNotification.add(toHireChangaNotificationDTO(notification)));
        return retNotification;
    }

    public HireChangaNotificationDTO toHireChangaNotificationDTO(Notification notification){

        return HireChangaNotificationDTO.builder()
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
    }
}
