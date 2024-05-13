package com.changas.model.status;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.Customer;
import com.changas.model.HiringTransaction;
import com.changas.model.status.handlers.*;

import java.util.List;

public abstract class TransactionStatusHandler {
    public static TransactionStatusHandler getHandlerFor(TransactionStatus status) throws TransactionStatusHandlerException {

        List<TransactionStatusHandler> handlers = List.of(
                new AwaitingProviderConfirmationHandler(),
                new AwaitingRequesterConfirmationHandler(),
                new DeclinedByProviderHandler(),
                new DeclinedByRequesterHandler(),
                new AcceptedByRequesterHandler()
        );

        return handlers
                .stream()
                .filter(handler -> handler.canHandle(status))
                .findAny()
                .orElseThrow(() -> new TransactionStatusHandlerException("Cannot instantiate a handler for the state \"." + status + "\""));

    }

    public abstract boolean canHandle(TransactionStatus status);
    public abstract void handleTransaction(HiringTransaction transaction, TransactionResponse response, Customer customer) throws IllegalTransactionOperationException;

    public void checkIfCanOperate(HiringTransaction transaction, Customer customer) throws IllegalTransactionOperationException {
        Long providerId = transaction.getProvider().getId();
        Long requesterId = transaction.getRequester().getId();
        Long customerId = customer.getId();

        if (!customerId.equals(providerId) && !customerId.equals(requesterId)) {
            throw new IllegalTransactionOperationException("Transaction cannot be operated by external customer");
        }

    }
}
