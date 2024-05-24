package com.changas.service;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.customer.EditCustomerRequest;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.customer.CustomerNotFoundException;
import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.changas.mappers.CustomerMapper.toCustomerOverviewDTO;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final AuthService authService;
    private final CustomerRepository customerRepository;

    public CustomerOverviewDTO getCustomerOverview(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return toCustomerOverviewDTO(customer);
    }

    @Transactional
    public CustomerOverviewDTO editCustomer(EditCustomerRequest request) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        request.getName().ifPresent(customer::setName);
        request.getPhotoUrl().ifPresent(customer::setPhotoUrl);
        customerRepository.save(customer);

        return toCustomerOverviewDTO(customer);

    }
}
