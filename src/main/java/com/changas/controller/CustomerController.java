package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/hirings")
    public ResponseEntity<ApiResponse<List<HiringOverviewDTO>>> getHiredTransactions() throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getAllTransactionsFromCustomer()));
    }

    @GetMapping("/pending-transactions")
    public ResponseEntity<ApiResponse<List<HiringOverviewDTO>>> getPendingTransactions() throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getPendingTransactionsAsProvider()));
    }

}
