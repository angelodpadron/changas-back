package com.changas.controller.utils;

import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final CustomerRepository customerRepository;

    public DataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }
}
