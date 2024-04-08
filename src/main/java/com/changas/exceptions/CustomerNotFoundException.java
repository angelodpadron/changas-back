package com.changas.exceptions;

public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(Long customerId) {
        super("No customer found with id " + customerId);
    }
}
