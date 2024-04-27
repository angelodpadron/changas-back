package com.changas.exceptions.customer;

public class CustomerAlreadyRegisteredException extends Exception{
    public CustomerAlreadyRegisteredException(String customerEmail) {
        super("User already registered with email: " + customerEmail);
    }
}
