package com.changas.model.status.handlers;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.ProviderProposal;
import com.changas.model.status.TransactionOperation;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;

public class AwaitingProviderConfirmationHandler extends TransactionStatusHandler {

    private final TransactionStatus statusToHandle = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;

    @Override
    public boolean canHandle(TransactionStatus status) {
        return status == statusToHandle;
    }

    @Override
    public void handleTransaction(HiringTransaction transaction, TransactionOperation operation, Customer customer) throws IllegalTransactionOperationException {
        checkIfCanOperate(transaction, customer);
        checkIfCanAnswer(transaction, customer);

        switch (operation.getResponse()) {
            case ACCEPT -> {

                ProviderProposal proposal = operation
                        .getProviderProposal()
                        .orElseThrow(() -> new IllegalTransactionOperationException("Need to provide a full proposal to accept request as provider"));

                transaction.setProviderProposal(proposal);
                transaction.setStatus(TransactionStatus.AWAITING_REQUESTER_CONFIRMATION);

            }
            case DECLINE -> transaction.setStatus(TransactionStatus.DECLINED_BY_PROVIDER);
        }

    }

    private void checkIfCanAnswer(HiringTransaction transaction, Customer customer) throws IllegalTransactionOperationException {
        if (!isProvider(transaction, customer)) {
            throw new IllegalTransactionOperationException("Only the provider can respond in the current transaction status");
        }
    }

    private boolean isProvider(HiringTransaction transaction, Customer customer) {
        return customer.getId().equals(transaction.getChanga().getProvider().getId());
    }
}
