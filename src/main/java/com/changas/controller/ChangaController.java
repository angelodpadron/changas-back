package com.changas.controller;

import com.changas.dto.ChangaOverviewDTO;
import com.changas.dto.HireChangaRequest;
import com.changas.service.ChangaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/changas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class ChangaController {

    private final ChangaService changaService;

    @Operation(summary = "Returns all the changas")
    @GetMapping
    public ResponseEntity<List<ChangaOverviewDTO>> getAllChangas() {
        return ResponseEntity.ok(changaService.getAllChangas());
    }

    @Operation(summary = "Return a changa with a given id")
    @GetMapping("/{changaId}")
    public ResponseEntity<ChangaOverviewDTO> getChangaWithId(@PathVariable Long changaId) throws Exception {
        return ResponseEntity.ok(changaService.getChangaById(changaId));
    }

    @Operation(summary = "Hire a give changa")
    @PostMapping("/hire")
    public ResponseEntity<?> hireChanga(@RequestBody HireChangaRequest hireChangaRequest) {
        try {
            changaService.hireChanga(hireChangaRequest.getChangaId(), hireChangaRequest.getCustomerId());
            return ResponseEntity.ok().body("Changa successfully hired.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
