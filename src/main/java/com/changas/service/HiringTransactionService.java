package com.changas.service;

import com.changas.dto.hiring.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.response.HiringResponse;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.mappers.HiringTransactionMapper;
import com.changas.model.*;
import com.changas.model.status.TransactionOperation;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;
import com.changas.repository.HiringTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.changas.mappers.HiringTransactionMapper.toHiringOverviewDTO;

@Service
@RequiredArgsConstructor
public class HiringTransactionService {
    private final HiringTransactionRepository transactionRepository;
    private final ChangaService changaService;
    private final AuthService authService;

    @Transactional
    public HiringOverviewDTO requestChanga(HireChangaRequest hireChangaRequest) throws CustomerNotAuthenticatedException, ChangaNotFoundException, HiringOwnChangaException {
        Customer requester = authService.getCustomerAuthenticated();
        Changa changa = changaService.getChangaById(hireChangaRequest.changaId());

        WorkAreaDetails workAreaDetails = WorkAreaDetails
                .builder()
                .description(hireChangaRequest.workAreaDetails().description())
                .photoUrl(hireChangaRequest.workAreaDetails().photoUrl())
                .build();

        HiringTransaction hiringTransaction = HiringTransaction.generateTransactionFor(changa, requester, workAreaDetails);

        transactionRepository.save(hiringTransaction);

        return toHiringOverviewDTO(hiringTransaction);
    }

    @Transactional
    public HiringOverviewDTO respondChangaRequest(HiringResponse response) throws CustomerNotAuthenticatedException, HiringTransactionNotFoundException, IllegalTransactionOperationException, TransactionStatusHandlerException {
        Customer customer = authService.getCustomerAuthenticated();
        HiringTransaction transaction = getHiringTransaction(response.getTransactionId());
        ProviderProposal providerProposal = null;

        if (response.getProviderProposal().isPresent()) {
            providerProposal = ProviderProposal
                    .builder()
                    .message(response.getProviderProposal().get().message())
                    .price(response.getProviderProposal().get().price())
                    .build();
        }

        TransactionOperation operation = new TransactionOperation(response.getResponse(), providerProposal);

        TransactionStatusHandler
                .getHandlerFor(transaction.getStatus())
                .handleTransaction(transaction, operation, customer);

        transactionRepository.save(transaction);

        return toHiringOverviewDTO(transaction);
    }

    public List<HiringOverviewDTO> getTransactionsFromCustomer() throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        return transactionRepository
                .allTransactionsFromCustomer(customer.getId())
                .stream()
                .map(HiringTransactionMapper::toHiringOverviewDTO)
                .collect(Collectors.toList());
    }

    public HiringOverviewDTO getTransactionFromCustomer(Long transactionId) throws CustomerNotAuthenticatedException, HiringTransactionNotFoundException {
        Customer customer = authService.getCustomerAuthenticated();
        return toHiringOverviewDTO(transactionRepository
                        .findCustomerTransactionById(transactionId, customer.getId())
                        .orElseThrow(() -> new HiringTransactionNotFoundException(transactionId)));
    }

    public List<HiringOverviewDTO> getTransactionsFromCustomerWithStatus(TransactionStatus status) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        return transactionRepository
                .findByCustomerIdAndStatus(customer.getId(), status)
                .stream()
                .map(HiringTransactionMapper::toHiringOverviewDTO)
                .collect(Collectors.toList());
    }

    private HiringTransaction getHiringTransaction(Long transactionId) throws HiringTransactionNotFoundException {
        return transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new HiringTransactionNotFoundException(transactionId));
    }



}
