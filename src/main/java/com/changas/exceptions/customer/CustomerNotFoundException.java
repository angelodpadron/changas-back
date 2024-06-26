package com.changas.exceptions.customer;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(Long customerId) {
        super("No customer found with id " + customerId);
    }

}
