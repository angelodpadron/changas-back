package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.customer.EditCustomerRequest;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Gets an overview of a customer")
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerOverviewDTO>> getCustomerOverview(@PathVariable Long customerId) throws CustomerNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomerOverview(customerId)));
    }

    @Operation(summary = "Gets all the changas posted by a customer")
    @GetMapping("/{customerId}/posts")
    public ResponseEntity<ApiResponse<List<ChangaOverviewDTO>>> getCustomerPosts(@PathVariable Long customerId) throws CustomerNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(customerService.getCustomerPosts(customerId)));
    }

    @Operation(summary = "Edit customer values")
    @PutMapping("/edit")
    public ResponseEntity<ApiResponse<CustomerOverviewDTO>> editCustomer(@RequestBody EditCustomerRequest editCustomerRequest) throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(customerService.editCustomer(editCustomerRequest)));
    }
}
