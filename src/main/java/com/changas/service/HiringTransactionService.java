package com.changas.service;

import com.changas.dto.hiring.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.ProviderProposalDTO;
import com.changas.dto.hiring.response.HiringResponse;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.*;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;
import com.changas.repository.HiringTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.changas.mappers.HiringTransactionMapper.toHiringOverviewDTO;

@Service
@RequiredArgsConstructor
public class HiringTransactionService {
    private final HiringTransactionRepository transactionRepository;
    private final ChangaService changaService;
    private final AuthService authService;

    @Transactional
    public HiringOverviewDTO hireChanga(HireChangaRequest hireChangaRequest) throws CustomerNotAuthenticatedException, ChangaNotFoundException, HiringOwnChangaException {
        Customer customer = getCustomerLoggedIn();
        Changa changa = getChanga(hireChangaRequest.changaId());
        Customer provider = changa.getProvider();

        checkIfCanHire(customer, provider);

        WorkAreaDetails workAreaDetails = WorkAreaDetails
                .builder()
                .description(hireChangaRequest.workAreaDetails().description())
                .photoUrl(hireChangaRequest.workAreaDetails().photoUrl())
                .build();

        HiringTransaction hiringTransaction = HiringTransaction
                .builder()
                .changa(changa)
                .provider(provider)
                .requester(customer)
                .workAreaDetails(workAreaDetails)
                .creationDate(Instant.now())
                .status(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION)
                .build();

        transactionRepository.save(hiringTransaction);

        return toHiringOverviewDTO(hiringTransaction);
    }

    @Transactional
    public HiringOverviewDTO respondChangaRequest(HiringResponse response) throws CustomerNotAuthenticatedException, HiringTransactionNotFoundException, IllegalTransactionOperationException, TransactionStatusHandlerException {
        Customer customer = getCustomerLoggedIn();
        HiringTransaction transaction = getHiringTransaction(response.getTransactionId());

        TransactionStatusHandler
                .getHandlerFor(transaction.getStatus())
                .handleTransaction(transaction, response.getResponse(), customer);

        updateProviderProposalIfRequired(response, customer, transaction);

        transactionRepository.save(transaction);

        return toHiringOverviewDTO(transaction);
    }

    private void checkIfCanHire(Customer customer, Customer provider) throws HiringOwnChangaException {
        if (customer.getId().equals(provider.getId())) {
            throw new HiringOwnChangaException();
        }
    }

    private void updateProviderProposalIfRequired(HiringResponse request, Customer customer, HiringTransaction transaction) {

        ProviderProposalDTO proposalDTO = request.getProviderProposal();
        boolean isProvider = customer.getId().equals(transaction.getProvider().getId());

        if (proposalDTO != null && isProvider) {
            ProviderProposal proposal = ProviderProposal
                    .builder()
                    .message(proposalDTO.message())
                    .price(proposalDTO.price())
                    .build();
            transaction.setProviderProposal(proposal);
        }
    }

    private Customer getCustomerLoggedIn() throws CustomerNotAuthenticatedException {
        return authService
                .getCustomerLoggedIn()
                .orElseThrow(CustomerNotAuthenticatedException::new);
    }

    private HiringTransaction getHiringTransaction(Long transactionId) throws HiringTransactionNotFoundException {
        return transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new HiringTransactionNotFoundException(transactionId));
    }

    private Changa getChanga(Long changaId) throws ChangaNotFoundException {
        return changaService
                .getChangaById(changaId)
                .orElseThrow(() -> new ChangaNotFoundException(changaId));
    }


}
