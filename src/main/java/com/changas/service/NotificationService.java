package com.changas.service;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.notification.HireChangaNotificationDTO;
import com.changas.exceptions.ResourceNotFoundException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.Notification;
import com.changas.repository.ChangaRepository;
import com.changas.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    //private final SimpMessagingTemplate template;
    private final ChangaRepository changaRepository;

    public void createNotification(Long changaId, Customer customer) throws ChangaNotFoundException {

        Changa changa = changaRepository.findById(changaId).orElseThrow(() -> new ChangaNotFoundException(changaId));
        createAndSaveNotification(changa, customer);
    }

    public void createAndSaveNotification(Changa changa, Customer customer) {

        Notification notification = Notification
                .builder()
                .customer(customer)
                .createdAt(Instant.now())
                .isRead(false)
                .changa(changa)
                .build();

        notificationRepository.save(notification);
        //changa.addHireChangaNotification(notification);
        //template.convertAndSendToUser(customer.getId().toString(), "/api/v1/notifications/hireChanga", notification);
    }

    public HireChangaNotificationDTO getNotificationById(Long idNotification) throws ResourceNotFoundException {
        Optional<Notification> optionalNotification = notificationRepository.findById(idNotification);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            return toHireChangaNotificationDTO(notification);
        }
        throw new ResourceNotFoundException("No hay notificacion con id: " + idNotification);
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
