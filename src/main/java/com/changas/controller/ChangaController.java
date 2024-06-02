package com.changas.controller;


import com.changas.dto.ApiResponse;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.changa.CreateChangaRequest;
import com.changas.dto.changa.UpdateChangaRequest;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.changa.UnauthorizedChangaEditException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.search.BadSearchRequestException;
import com.changas.service.ChangaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/changas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class ChangaController {

    private final ChangaService changaService;

    @Operation(summary = "Returns all the changas")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChangaOverviewDTO>>> getAllChangas() {
        return ResponseEntity.ok(ApiResponse.success(changaService.getAllChangaOverviews()));
    }

    @Operation(summary = "Return a changa with a given id")
    @GetMapping("/{changaId}")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> getChangaWithId(@PathVariable Long changaId) throws ChangaNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(changaService.getChangaOverviewById(changaId)));
    }

    @Operation(summary = "Return changas that meet the search criteria")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Set<ChangaOverviewDTO>>> findChangasWithTopics(@RequestParam(required = false) Optional<String> title, @RequestParam(required = false) Optional<Set<String>> topics) throws BadSearchRequestException {
        return ResponseEntity.ok(ApiResponse.success(changaService.findChangaByCriteriaHandler(title, topics)));
    }

    @Operation(summary = "Create a new changa")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> createChanga(@RequestBody CreateChangaRequest createChangaRequest) throws CustomerNotAuthenticatedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(changaService.createChanga(createChangaRequest)));
    }

    @Operation(summary = "Update a changa")
    @PutMapping("/{changaId}/edit")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> editChanga(@PathVariable Long changaId, @RequestBody UpdateChangaRequest updateRequest) throws CustomerNotAuthenticatedException, UnauthorizedChangaEditException, ChangaNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(changaService.updateChanga(changaId, updateRequest)));
    }

    @Operation(summary = "Delete a changa")
    @DeleteMapping("/{changaId}/delete")
    public ResponseEntity<ApiResponse<ChangaOverviewDTO>> deleteChanga(@PathVariable Long changaId) throws CustomerNotAuthenticatedException, UnauthorizedChangaEditException, ChangaNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(changaService.deactivateChanga(changaId)));
    }

}
