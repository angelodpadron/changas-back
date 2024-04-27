package com.changas.controller;

import com.changas.dto.notification.HireChangaNotificationDTO;
import com.changas.exceptions.ResourceNotFoundException;
import com.changas.dto.*;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Customer;
import com.changas.service.AuthService;
import com.changas.service.ChangaService;
import com.changas.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/changas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class ChangaController {

    private final ChangaService changaService;
    private final AuthService authService;
    private final NotificationService notificationService;

    @Operation(summary = "Returns all the changas")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChangaOverviewDTO>>> getAllChangas() {
        return ResponseEntity.ok(ApiResponse.success(changaService.getAllChangas()));
    }

    @Operation(summary = "Return a changa with a given id")
    @GetMapping("/{changaId}")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> getChangaWithId(@PathVariable Long changaId) throws ChangaNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(changaService.getChangaById(changaId)));
    }

    @Operation(summary = "Hire a give changa")
    @PostMapping("/hire")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> hireChanga(@RequestBody HireChangaRequest hireChangaRequest) throws ChangaNotFoundException, CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        HiringOverviewDTO hiringOverview = changaService.hireChanga(hireChangaRequest.changaId(), customer);
        notificationService.createNotification(hireChangaRequest.changaId(),customer);
        return ResponseEntity.ok(ApiResponse.success(hiringOverview));
    }

    @Operation(summary = "Search changa by title")
    @GetMapping("/search/{title}")
    public ResponseEntity<List<ChangaOverviewDTO>> searchChangaByTitle(@PathVariable String title){
        try {
            return ResponseEntity.ok(changaService.getChangaByTitleContainsIgnoreCase(title));
        }
        catch (Exception e){
            throw new ResourceNotFoundException("No hay Changas segun el titulo: " + title);
        }
    }

    /*
    @GetMapping("/notifications/{changaid}")
    public ResponseEntity<List<HireChangaNotificationDTO>> getNotificationsByChanga(Long changaId) throws Exception{
        List<HireChangaNotificationDTO> notifications = changaService.getNotificationsByChangaId(changaId);
        return ResponseEntity.ok().body(notifications);
    }
    */
    @GetMapping("/notifications/{changaId}")
    public ResponseEntity<List<HireChangaNotificationDTO>> getNotificationsByChanga(@PathVariable Long changaId) {
        try {
            List<HireChangaNotificationDTO> notifications = changaService.getNotificationsByChangaId(changaId);
            return ResponseEntity.ok().body(notifications);
        } catch (Exception e) {
            throw new ResourceNotFoundException("No hay changa con id: " + changaId);
        }
    }
}
