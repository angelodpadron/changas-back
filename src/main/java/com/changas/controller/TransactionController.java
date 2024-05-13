package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.response.HiringResponse;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.service.HiringTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8100")
public class TransactionController {

    private final HiringTransactionService transactionService;

    @Operation(summary = "Accepts or decline a given hiring request")
    @PostMapping("/respond-request")
    public ResponseEntity<ApiResponse<HiringOverviewDTO>> respondHiringRequest(@RequestBody HiringResponse response) throws HiringTransactionNotFoundException, CustomerNotAuthenticatedException, IllegalTransactionOperationException, TransactionStatusHandlerException {
        return ResponseEntity.ok(ApiResponse.success(transactionService.respondChangaRequest(response)));
    }


}
