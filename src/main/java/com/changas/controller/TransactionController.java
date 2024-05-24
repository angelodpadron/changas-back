package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.hiring.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.response.HiringResponse;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.status.TransactionStatus;
import com.changas.service.HiringTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8100")
public class TransactionController {

    private final HiringTransactionService transactionService;

    @Operation(summary = "Retrieves all the changa transactions from a customer")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<HiringOverviewDTO>>> getAllHiringsFromCustomer() throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsFromCustomer()));
    }

    @Operation(summary = "Retrieve the details of a given transaction")
    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> getHiringOverview(@PathVariable Long transactionId) throws HiringTransactionNotFoundException, CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionFromCustomer(transactionId)));
    }

    @Operation(summary = "Retrieve all the transactions of the customer with a given status")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<HiringOverviewDTO>>> getTransactionsWithStatus(@RequestParam TransactionStatus status) throws CustomerNotAuthenticatedException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionsFromCustomerWithStatus(status)));
    }

    @Operation(summary = "Request a changa")
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> requestChanga(@RequestBody HireChangaRequest hireChangaRequest) throws ChangaNotFoundException, CustomerNotAuthenticatedException, HiringOwnChangaException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.requestChanga(hireChangaRequest)));
    }
    @Operation(summary = "Accepts or decline a given hiring request")
    @PostMapping("/respond-request")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> respondRequest(@RequestBody HiringResponse response) throws HiringTransactionNotFoundException, CustomerNotAuthenticatedException, IllegalTransactionOperationException, TransactionStatusHandlerException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.respondChangaRequest(response)));
    }






}
