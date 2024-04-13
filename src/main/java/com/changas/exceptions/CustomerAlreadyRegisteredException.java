package com.changas.exceptions;

public class CustomerAlreadyRegisteredException extends Exception{
    public CustomerAlreadyRegisteredException(String customerEmail) {
        super("User already registered with email: " + customerEmail);
    }
}
