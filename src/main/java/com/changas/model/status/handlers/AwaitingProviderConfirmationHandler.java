package com.changas.model.status.handlers;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionResponse;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;

public class AwaitingProviderConfirmationHandler extends TransactionStatusHandler {

    private final TransactionStatus statusToHandle = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;

    @Override
    public boolean canHandle(TransactionStatus status) {
        return status == statusToHandle;
    }

    @Override
    public void handleTransaction(HiringTransaction transaction, TransactionResponse response, Customer customer) throws IllegalTransactionOperationException {
        checkIfCanAnswer(transaction, customer);

        switch (response) {
            case ACCEPT -> transaction.setStatus(TransactionStatus.ACCEPTED_BY_PROVIDER);
            case DECLINE -> transaction.setStatus(TransactionStatus.DECLINED_BY_PROVIDER);
        }

    }

    private void checkIfCanAnswer(HiringTransaction transaction, Customer customer) throws IllegalTransactionOperationException {
        if (!isProvider(transaction, customer)) {
            throw new IllegalTransactionOperationException("Only the provider can respond in the current transaction status");
        }
    }

    private boolean isProvider(HiringTransaction transaction, Customer customer) {
        return customer.getId().equals(transaction.getProvider().getId());
    }
}
