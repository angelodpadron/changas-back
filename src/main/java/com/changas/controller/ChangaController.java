package com.changas.controller;

import com.changas.model.Changa;
import com.changas.service.ChangaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/changas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class ChangaController {

    private final ChangaService changaService;

    @Operation(summary = "Returns all the changas")
    @GetMapping
    public ResponseEntity<List<Changa>> getAllChangas() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(changaService.getAllChangas());
    }


}
