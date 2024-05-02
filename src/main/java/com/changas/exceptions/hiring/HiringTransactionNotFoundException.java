package com.changas.exceptions.hiring;

public class HiringTransactionNotFoundException extends Exception {
    public HiringTransactionNotFoundException(Long transactionId) {
        super("Could not found transaction with id: " + transactionId);
    }
}
