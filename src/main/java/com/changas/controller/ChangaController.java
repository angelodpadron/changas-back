package com.changas.controller;

import com.changas.dto.*;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.changa.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.service.AuthService;
import com.changas.service.ChangaService;
import com.changas.service.HiringTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/changas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class ChangaController {

    private final ChangaService changaService;
    private final HiringTransactionService hiringTransactionService;
    private final AuthService authService;

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

    @Operation(summary = "Hire a give changa")
    @PostMapping("/hire")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> hireChanga(@RequestBody HireChangaRequest hireChangaRequest) throws ChangaNotFoundException, CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        Changa changa = changaService.getChangaById(hireChangaRequest.changaId()).orElseThrow(() -> new ChangaNotFoundException(hireChangaRequest.changaId()));
        HiringOverviewDTO hiringOverview = hiringTransactionService.hireChanga(changa, customer);
        return ResponseEntity.ok(ApiResponse.success(hiringOverview));
    }

    @Operation(summary = "Create a new changa")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> createChanga(@RequestBody @Valid CreateChangaRequest createChangaRequest) throws CustomerNotAuthenticatedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(changaService.createChanga(createChangaRequest)));
    }

}
