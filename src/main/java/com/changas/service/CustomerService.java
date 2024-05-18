package com.changas.service;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.customer.UpdateCustomerRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.mappers.HiringTransactionMapper;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionStatus;
import com.changas.repository.CustomerRepository;
import com.changas.repository.HiringTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.changas.mappers.CustomerMapper.toCustomerOverviewDTO;
import static com.changas.mappers.HiringTransactionMapper.toHiringOverviewDTO;

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

    private List<HiringOverviewDTO> toHiringTransactionDTOList(Set<HiringTransaction> transactions) {
        return transactions.stream().map(HiringTransactionMapper::toHiringOverviewDTO).collect(Collectors.toList());
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
