package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.customer.UpdateCustomerRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.model.status.TransactionStatus;
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

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerOverviewDTO>> getCustomerOverview(@PathVariable Long customerId) throws CustomerNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomerOverview(customerId)));
    }

    @GetMapping("/hirings")
    public ResponseEntity<ApiResponse<List<HiringOverviewDTO>>> getHiredTransactions() throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getAllTransactionsFromCustomer()));
    }

    @GetMapping("/hirings/{transactionId}")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> getCustomerTransaction(@PathVariable Long transactionId) throws HiringTransactionNotFoundException, CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getTransactionWithIdFromCustomer(transactionId)));
    }

    @GetMapping("/hirings/filter")
    public ResponseEntity<ApiResponse<List<HiringOverviewDTO>>> getTransactionsWithStatus(@RequestParam TransactionStatus status) throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getTransactionWithStatus(status)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerOverviewDTO>> editProfile(@RequestBody UpdateCustomerRequest updateRequest) throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(customerService.updateProfile(updateRequest)));
    }
}
