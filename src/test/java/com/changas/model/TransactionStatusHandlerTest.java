package com.changas.model;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.status.TransactionResponse;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;
import com.changas.model.status.handlers.AwaitingProviderConfirmationHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionStatusHandlerTest {

    @DisplayName("A transaction handler knows if it can handle a status")
    @Test
    void transactionHandlerKnowIfItCanHandleAStatusTest() {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        AwaitingProviderConfirmationHandler handler = new AwaitingProviderConfirmationHandler();

        assertTrue(handler.canHandle(status));
    }

    @DisplayName("A transaction status handler returns a handler for a given status")
    @Test
    void transactionStatusHandlerReturnsAHandlerForTheStatusTest() throws TransactionStatusHandlerException {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        TransactionStatusHandler handler = TransactionStatusHandler.getHandlerFor(status);

        assertEquals(AwaitingProviderConfirmationHandler.class, handler.getClass());
    }

    @DisplayName("A transaction awaiting the response of provider changes it's status to accepted")
    @Test
    void transactionAwaitingProviderResponseChangesToAcceptedTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        HiringTransaction transaction = mock(HiringTransaction.class);
        Customer provider = mock(Customer.class);
        Long providerId = 1L;

        when(transaction.getStatus()).thenReturn(status);
        when(transaction.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(providerId);

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, TransactionResponse.ACCEPT, provider);

        verify(transaction).setStatus(TransactionStatus.ACCEPTED_BY_PROVIDER);

    }

    @DisplayName("A transaction awaiting the response of provider changes it's status to declined")
    @Test
    void transactionAwaitingProviderResponseChangesToDeclinedTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        HiringTransaction transaction = mock(HiringTransaction.class);
        Customer provider = mock(Customer.class);
        Long providerId = 1L;

        when(transaction.getStatus()).thenReturn(status);
        when(transaction.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(providerId);

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, TransactionResponse.DECLINE, provider);

        verify(transaction).setStatus(TransactionStatus.DECLINED_BY_PROVIDER);

    }

    @DisplayName("Only the provider can answer a transaction with an 'awaiting provider confirmation' status")
    @Test
    void onlyAProviderCanAnswerAnAwaitingProviderConfirmationTransactionTest() {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        HiringTransaction transaction = mock(HiringTransaction.class);
        Customer provider = mock(Customer.class);
        Long providerId = 1L;
        Customer requester = mock(Customer.class);
        Long customerId = 2L;

        when(transaction.getStatus()).thenReturn(status);
        when(transaction.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(providerId);
        when(requester.getId()).thenReturn(customerId);

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler.getHandlerFor(status).handleTransaction(transaction, TransactionResponse.ACCEPT, requester));

    }


}
