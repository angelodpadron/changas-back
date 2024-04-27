package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.changa.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.service.ChangaService;
import com.changas.service.HiringTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
