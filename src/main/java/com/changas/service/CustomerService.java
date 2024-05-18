package com.changas.service;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.customer.UpdateCustomerRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionStatus;
import com.changas.repository.CustomerRepository;
import com.changas.repository.HiringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final AuthService authService;
    private final CustomerRepository customerRepository;
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

    public HiringOverviewDTO getTransactionWithIdFromCustomer(Long transactionId) throws HiringTransactionNotFoundException, CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);
        HiringTransaction transaction = hiringTransactionRepository.findCustomerTransactionById(transactionId, customer.getId()).orElseThrow(() -> new HiringTransactionNotFoundException(transactionId));
        return toHiringOverviewDTO(transaction);
    }

    public CustomerOverviewDTO getCustomerOverview(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
        return toCustomerOverviewDTO(customer);
    }

    private CustomerOverviewDTO toCustomerOverviewDTO(Customer customer) {
        return CustomerOverviewDTO
                .builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .photoUrl(customer.getPhotoUrl())
                .build();
    }

    private HiringOverviewDTO toHiringOverviewDTO(HiringTransaction transaction) {
        return HiringOverviewDTO
                .builder()
                .hiringId(transaction.getId())
                .changaId(transaction.getChanga().getId())
                .customerId(transaction.getRequester().getId())
                .providerId(transaction.getProvider().getId())
                .changaTitle(transaction.getChanga().getTitle())
                .changaDescription(transaction.getChanga().getDescription())
                .changaPhotoUrl(transaction.getChanga().getPhotoUrl())
                .creationDate(Instant.now())
                .workDetails(transaction.getWorkDetails())
                .workAreaPhotoUrl(transaction.getWorkAreaPhotoUrl())
                .status(transaction.getStatus())
                .build();
    }

    private List<HiringOverviewDTO> toHiringTransactionDTOList(Set<HiringTransaction> transactions) {
        List<HiringOverviewDTO> hirings = new ArrayList<>();
        transactions.forEach(transaction -> hirings.add(toHiringOverviewDTO(transaction)));
        return hirings;
    }

    @Transactional
    public CustomerOverviewDTO updateProfile(UpdateCustomerRequest request) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerLoggedIn().orElseThrow(CustomerNotAuthenticatedException::new);

        if (request.getName() != null) customer.setName(request.getName());
        if (request.getPhotoUrl() != null) customer.setPhotoUrl(request.getPhotoUrl());

        customerRepository.save(customer);

        return toCustomerOverviewDTO(customer);

    }
}
