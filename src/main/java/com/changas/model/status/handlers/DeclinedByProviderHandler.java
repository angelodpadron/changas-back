package com.changas.model.status.handlers;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionOperation;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;

public class DeclinedByProviderHandler extends TransactionStatusHandler {

    private final TransactionStatus statusToHandle = TransactionStatus.DECLINED_BY_PROVIDER;

    @Override
    public boolean canHandle(TransactionStatus status) {
        return status == statusToHandle;
    }

    @Override
    public void handleTransaction(HiringTransaction transaction, TransactionOperation operation, Customer customer) throws IllegalTransactionOperationException {
        checkIfCanOperate(transaction, customer);
        throw new IllegalTransactionOperationException("The transaction has been declined by the provider and cannot longer be operated");
    }
}
