package com.changas.service;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionStatus;
import com.changas.repository.HiringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final AuthService authService;
    private final HiringTransactionRepository hiringTransactionRepository;

    public List<HiringOverviewDTO> getAllTransactionsFromCustomer() throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        Set<HiringTransaction> transactions = hiringTransactionRepository.allCustomerTransactions(customer.getId());
        return toHiringTransactionDTOList(transactions);
    }

    public List<HiringOverviewDTO> getPendingTransactionsAsProvider() throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        Set<HiringTransaction> pendingTransactions =
                hiringTransactionRepository.findByProviderIdAndStatus(customer.getId(), TransactionStatus.AWAITING_PROVIDER_CONFIRMATION);
        return toHiringTransactionDTOList(pendingTransactions);

    }

    private List<HiringOverviewDTO> toHiringTransactionDTOList(Set<HiringTransaction> transactions) {
        List<HiringOverviewDTO> hirings = new ArrayList<>();

        transactions.forEach(transaction -> {
            HiringOverviewDTO hiringOverview = HiringOverviewDTO
                    .builder()
                    .hiringId(transaction.getId())
                    .changaId(transaction.getChanga().getId())
                    .customerId(transaction.getRequester().getId())
                    .providerId(transaction.getProvider().getId())
                    .changaTitle(transaction.getChanga().getTitle())
                    .changaDescription(transaction.getChanga().getDescription())
                    .changaPhotoUrl(transaction.getChanga().getPhotoUrl())
                    .creationDate(Instant.now())
                    .status(transaction.getStatus())
                    .build();

            hirings.add(hiringOverview);

        });

        return hirings;
    }
}
