package com.changas.service;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.TransactionStatus;
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
        return toHiringTransactionDTOList(customer.getTransactions());
    }

    public List<HiringOverviewDTO> getPendingTransactionsAsProvider() throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        Set<HiringTransaction> pendingTransactions =
                hiringTransactionRepository.findByProviderIdAndStatus(customer.getId(), TransactionStatus.AWAITING_PROVIDER_CONFIRMATION);
        return toHiringTransactionDTOList(pendingTransactions);

    }

    private List<HiringOverviewDTO> toHiringTransactionDTOList(Set<HiringTransaction> transactions) {
        List<HiringOverviewDTO> hirings = new ArrayList<>();

        transactions.forEach(hiringTransaction -> {
            HiringOverviewDTO hiringOverview = HiringOverviewDTO
                    .builder()
                    .hiringId(hiringTransaction.getId())
                    .changaId(hiringTransaction.getChanga().getId())
                    .customerId(hiringTransaction.getRequester().getId())
                    .providerId(hiringTransaction.getProvider().getId())
                    .changaTitle(hiringTransaction.getChanga().getTitle())
                    .changaDescription(hiringTransaction.getChanga().getDescription())
                    .changaPhotoUrl(hiringTransaction.getChanga().getPhotoUrl())
                    .creationDate(Instant.now())
                    .status(hiringTransaction.getStatus())
                    .build();

            hirings.add(hiringOverview);

        });

        return hirings;
    }
}
