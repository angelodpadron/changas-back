package com.changas.service;

import com.changas.dto.changa.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.HiringResponseRequest;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;
import com.changas.repository.HiringTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HiringTransactionService {
    private final HiringTransactionRepository transactionRepository;
    private final ChangaService changaService;
    private final AuthService authService;

    @Transactional
    public HiringOverviewDTO hireChanga(HireChangaRequest hireChangaRequest) throws CustomerNotAuthenticatedException, ChangaNotFoundException, HiringOwnChangaException {
        Customer customer = authService
                .getCustomerLoggedIn()
                .orElseThrow(CustomerNotAuthenticatedException::new);

        Changa changa = changaService
                .getChangaById(hireChangaRequest.changaId())
                .orElseThrow(() -> new ChangaNotFoundException(hireChangaRequest.changaId()));

        Customer provider = changa.getProvider();

        if (customer.getId().equals(provider.getId())) {
            throw new HiringOwnChangaException();
        }

        HiringTransaction hiringTransaction = HiringTransaction
                .builder()
                .changa(changa)
                .provider(provider)
                .requester(customer)
                .creationDate(Instant.now())
                .workDetails(hireChangaRequest.workDetails())
                .workAreaPhotoUrl(hireChangaRequest.workAreaPhotoUrl())
                .status(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION)
                .build();

        transactionRepository.save(hiringTransaction);

        return asHiringOverviewDTO(hiringTransaction);
    }

    public HiringOverviewDTO answerChangaRequest(HiringResponseRequest hiringResponseRequest) throws CustomerNotAuthenticatedException, HiringTransactionNotFoundException, IllegalTransactionOperationException, TransactionStatusHandlerException {
        Customer customer = authService
                .getCustomerLoggedIn()
                .orElseThrow(CustomerNotAuthenticatedException::new);

        HiringTransaction transaction = transactionRepository
                .findById(hiringResponseRequest.transactionId())
                .orElseThrow(() -> new HiringTransactionNotFoundException(hiringResponseRequest.transactionId()));

        TransactionStatusHandler
                .getHandlerFor(transaction.getStatus())
                .handleTransaction(transaction, hiringResponseRequest.response(), customer);

        transactionRepository.save(transaction);

        return asHiringOverviewDTO(transaction);

    }

    private HiringOverviewDTO asHiringOverviewDTO(HiringTransaction hiringTransaction) {
        return HiringOverviewDTO
                .builder()
                .hiringId(hiringTransaction.getId())
                .changaId(hiringTransaction.getChanga().getId())
                .providerId(hiringTransaction.getChanga().getProvider().getId())
                .customerId(hiringTransaction.getRequester().getId())
                .changaPhotoUrl(hiringTransaction.getChanga().getPhotoUrl())
                .changaDescription(hiringTransaction.getChanga().getDescription())
                .changaTitle(hiringTransaction.getChanga().getTitle())
                .creationDate(hiringTransaction.getCreationDate())
                .workDetails(hiringTransaction.getWorkDetails())
                .workAreaPhotoUrl(hiringTransaction.getWorkAreaPhotoUrl())
                .status(hiringTransaction.getStatus())
                .build();
    }

}
