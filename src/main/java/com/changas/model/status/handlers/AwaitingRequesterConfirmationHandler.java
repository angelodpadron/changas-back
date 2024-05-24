package com.changas.model.status.handlers;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.TransactionOperation;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;

public class AwaitingRequesterConfirmationHandler extends TransactionStatusHandler {
    private final TransactionStatus statusToHandle = TransactionStatus.AWAITING_REQUESTER_CONFIRMATION;
    @Override
    public boolean canHandle(TransactionStatus status) {
        return status == statusToHandle;
    }

    @Override
    public void handleTransaction(HiringTransaction transaction, TransactionOperation operation, Customer customer) throws IllegalTransactionOperationException {
        checkIfCanOperate(transaction, customer);
        checkIfCanAnswer(transaction, customer);

        switch (operation.getResponse()) {
            case ACCEPT -> transaction.setStatus(TransactionStatus.ACCEPTED_BY_REQUESTER);
            case DECLINE -> transaction.setStatus(TransactionStatus.DECLINED_BY_REQUESTER);
        }
    }

    private void checkIfCanAnswer(HiringTransaction transaction, Customer customer) throws IllegalTransactionOperationException {
        if (!isRequester(transaction, customer)) {
            throw new IllegalTransactionOperationException("Only the requester can respond in the current transaction status");
        }
    }

    private boolean isRequester(HiringTransaction transaction, Customer customer) {
        return customer.getId().equals(transaction.getRequester().getId());
    }
}
