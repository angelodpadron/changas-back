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
        Customer customer = authService.getCustomerAuthenticated();
        return hiringTransactionRepository
                .allCustomerTransactions(customer.getId())
                .stream()
                .map(HiringTransactionMapper::toHiringOverviewDTO)
                .collect(Collectors.toList());
    }

    public HiringOverviewDTO getTransactionWithIdFromCustomer(Long transactionId) throws HiringTransactionNotFoundException, CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        HiringTransaction transaction = hiringTransactionRepository
                .findCustomerTransactionById(transactionId, customer.getId())
                .orElseThrow(() -> new HiringTransactionNotFoundException(transactionId));
        return toHiringOverviewDTO(transaction);
    }

    public List<HiringOverviewDTO> getTransactionWithStatus(TransactionStatus status) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        return hiringTransactionRepository
                .findByProviderIdAndStatus(customer.getId(), status)
                .stream()
                .map(HiringTransactionMapper::toHiringOverviewDTO)
                .collect(Collectors.toList());

    }

    public CustomerOverviewDTO getCustomerOverview(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return toCustomerOverviewDTO(customer);
    }

    @Transactional
    public CustomerOverviewDTO updateProfile(UpdateCustomerRequest request) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();

        request.getName().ifPresent(customer::setName);
        request.getPhotoUrl().ifPresent(customer::setPhotoUrl);

        customerRepository.save(customer);

        return toCustomerOverviewDTO(customer);

    }
}
