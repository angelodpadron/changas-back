package com.changas.model;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.status.TransactionResponse;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;
import com.changas.model.status.handlers.AwaitingProviderConfirmationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionStatusHandlerTest {

    HiringTransaction transaction;
    Customer provider;
    Customer requester;

    @BeforeEach
    void setup() {
        transaction = mock(HiringTransaction.class);
        provider = mock(Customer.class);
        requester = mock(Customer.class);

        when(transaction.getProvider()).thenReturn(provider);
        when(transaction.getRequester()).thenReturn(requester);
        when(provider.getId()).thenReturn(1L);
        when(requester.getId()).thenReturn(2L);
    }

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

    @DisplayName("Only the party of a transaction can operate it")
    @Test
    void operatingATransactionAsAnExternalCustomerThrowsExceptionTest() {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        when(transaction.getStatus()).thenReturn(status);

        Customer externalCustomer = mock(Customer.class);
        when(externalCustomer.getId()).thenReturn(3L);

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, TransactionResponse.ACCEPT, externalCustomer));

    }

    @DisplayName("Only the provider can answer a transaction with an 'awaiting provider confirmation' status")
    @Test
    void onlyAProviderCanAnswerAnAwaitingProviderConfirmationTransactionTest() {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;

        when(transaction.getStatus()).thenReturn(status);

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, TransactionResponse.ACCEPT, requester));

    }

    @DisplayName("A transaction awaiting the response of provider changes it's status to 'awaiting requester confirmation'")
    @Test
    void transactionAwaitingProviderResponseChangesToAcceptedTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;

        when(transaction.getStatus()).thenReturn(status);

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, TransactionResponse.ACCEPT, provider);

        verify(transaction).setStatus(TransactionStatus.AWAITING_REQUESTER_CONFIRMATION);

    }

    @DisplayName("A transaction awaiting the response of provider changes it's status to declined")
    @Test
    void transactionAwaitingProviderResponseChangesToDeclinedTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, TransactionResponse.DECLINE, provider);

        verify(transaction).setStatus(TransactionStatus.DECLINED_BY_PROVIDER);

    }

    @DisplayName("Only the requester can answer a transaction with an 'awaiting requester confirmation' status")
    @Test
    void providerAttempToRespondAnAwaitingRequesterConfirmationTest() {
        TransactionStatus status = TransactionStatus.AWAITING_REQUESTER_CONFIRMATION;

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, TransactionResponse.ACCEPT, provider));
    }

    @DisplayName("A transaction awaiting the response of requester changes it's status to 'accepted by requester'")
    @Test
    void requesterAcceptsTransactionTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_REQUESTER_CONFIRMATION;

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, TransactionResponse.ACCEPT, requester);

        verify(transaction).setStatus(TransactionStatus.ACCEPTED_BY_REQUESTER);

    }

    @DisplayName("A transaction awaiting the response of requester changes it's status to 'declined by requester'")
    @Test
    void requesterDeclinesTransactionTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_REQUESTER_CONFIRMATION;

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, TransactionResponse.DECLINE, requester);

        verify(transaction).setStatus(TransactionStatus.DECLINED_BY_REQUESTER);

    }

    @DisplayName("A transaction with a 'declined by provider' status cannot change it's status")
    @Test
    void declinedTransactionCannotChangeItsStatusTest() {
        TransactionStatus status = TransactionStatus.DECLINED_BY_PROVIDER;

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, TransactionResponse.ACCEPT, provider));
    }

}
