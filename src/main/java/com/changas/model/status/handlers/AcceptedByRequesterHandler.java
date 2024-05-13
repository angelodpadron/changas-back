package com.changas.model.status.handlers;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionResponse;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;

public class AcceptedByRequesterHandler extends TransactionStatusHandler {
    private final TransactionStatus statusToHandle = TransactionStatus.ACCEPTED_BY_REQUESTER;
    @Override
    public boolean canHandle(TransactionStatus status) {
        return status == statusToHandle;
    }

    @Override
    public void handleTransaction(HiringTransaction transaction, TransactionResponse response, Customer customer) throws IllegalTransactionOperationException {
        checkIfCanOperate(transaction, customer);
        throw new IllegalTransactionOperationException("The transaction has been accepted by the requester and cannot longer be operated");
    }
}
