package com.changas.controller;


import com.changas.dto.notification.HireChangaNotificationDTO;
import com.changas.exceptions.ResourceNotFoundException;

import com.changas.dto.ApiResponse;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.changa.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Customer;
import com.changas.service.AuthService;
import com.changas.service.ChangaService;
import com.changas.service.NotificationService;
import com.changas.service.HiringTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/changas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class ChangaController {

    private final ChangaService changaService;
    private final AuthService authService;
    private final NotificationService notificationService;
    private final HiringTransactionService hiringTransactionService;


    @Operation(summary = "Returns all the changas")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChangaOverviewDTO>>> getAllChangas() {
        return ResponseEntity.ok(ApiResponse.success(changaService.getAllChangas()));
    }

    @Operation(summary = "Return a changa with a given id")
    @GetMapping("/{changaId}")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> getChangaWithId(@PathVariable Long changaId) throws ChangaNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(changaService.getChangaOverviewById(changaId)));
    }

    @Operation(summary = "Return changas that contain certain topics")
    @GetMapping("/findBy")
    public ResponseEntity<ApiResponse<Set<ChangaOverviewDTO>>> findChangasWithTopics(@RequestParam Set<String> topics) {
        return ResponseEntity.ok(ApiResponse.success(changaService.findChangaWithTopics(topics)));
    }

    @Operation(summary = "Hire a give changa")
    @PostMapping("/hire")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> hireChanga(@RequestBody HireChangaRequest hireChangaRequest) throws ChangaNotFoundException, CustomerNotAuthenticatedException, HiringOwnChangaException {
        return ResponseEntity.ok(ApiResponse.success(hiringTransactionService.hireChanga(hireChangaRequest)));
    }

    @Operation(summary = "Create a new changa")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> createChanga(@RequestBody @Valid CreateChangaRequest createChangaRequest) throws CustomerNotAuthenticatedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(changaService.createChanga(createChangaRequest)));

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
