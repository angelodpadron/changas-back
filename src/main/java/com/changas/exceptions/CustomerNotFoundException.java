package com.changas.exceptions;

public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
