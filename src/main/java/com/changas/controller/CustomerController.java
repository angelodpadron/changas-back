package com.changas.controller;

import com.changas.dto.HiringOverviewDTO;
import com.changas.exceptions.CustomerNotFoundException;
import com.changas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{customerId}/hirings")
    public ResponseEntity<List<HiringOverviewDTO>> getHiredTransactions(@PathVariable Long customerId) throws CustomerNotFoundException {
        return ResponseEntity.ok(customerService.getHiringsFromCustomer(customerId));
    }

}
