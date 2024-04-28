package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.notification.HireChangaNotificationDTO;
import com.changas.exceptions.ResourceNotFoundException;
import com.changas.model.Notification;
import com.changas.model.ResponseMessage;
import com.changas.service.ChangaService;
import com.changas.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<HireChangaNotificationDTO>> getAllNotifications(){
        List <HireChangaNotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok().body(notifications);
    }
}
