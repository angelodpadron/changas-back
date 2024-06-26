package com.changas.model;

import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.status.TransactionOperation;
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
    Changa changa;

    @BeforeEach
    void setup() {
        transaction = mock(HiringTransaction.class);
        provider = mock(Customer.class);
        requester = mock(Customer.class);
        changa = mock(Changa.class);

        when(transaction.getRequester()).thenReturn(requester);
        when(transaction.getChanga()).thenReturn(changa);
        when(provider.getId()).thenReturn(1L);
        when(requester.getId()).thenReturn(2L);
        when(changa.getProvider()).thenReturn(provider);
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

        TransactionOperation operation = new TransactionOperation(TransactionResponse.ACCEPT, new ProviderProposal());

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, operation, externalCustomer));

    }

    @DisplayName("Only the provider can answer a transaction with an 'awaiting provider confirmation' status")
    @Test
    void onlyAProviderCanAnswerAnAwaitingProviderConfirmationTransactionTest() {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        TransactionOperation operation = new TransactionOperation(TransactionResponse.ACCEPT, new ProviderProposal());


        when(transaction.getStatus()).thenReturn(status);

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, operation, requester));

    }

    @DisplayName("A transaction awaiting the response of provider changes it's status to 'awaiting requester confirmation'")
    @Test
    void transactionAwaitingProviderResponseChangesToAcceptedTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;

        when(transaction.getStatus()).thenReturn(status);

        TransactionOperation operation = new TransactionOperation(TransactionResponse.ACCEPT, new ProviderProposal());

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, operation, provider);

        verify(transaction).setStatus(TransactionStatus.AWAITING_REQUESTER_CONFIRMATION);

    }

    @DisplayName("A transaction awaiting the response of provider changes it's status to declined")
    @Test
    void transactionAwaitingProviderResponseChangesToDeclinedTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_PROVIDER_CONFIRMATION;
        TransactionOperation operation = new TransactionOperation(TransactionResponse.DECLINE, new ProviderProposal());

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, operation, provider);

        verify(transaction).setStatus(TransactionStatus.DECLINED_BY_PROVIDER);

    }

    @DisplayName("Only the requester can answer a transaction with an 'awaiting requester confirmation' status")
    @Test
    void providerAttempToRespondAnAwaitingRequesterConfirmationTest() {
        TransactionStatus status = TransactionStatus.AWAITING_REQUESTER_CONFIRMATION;
        TransactionOperation operation = new TransactionOperation(TransactionResponse.ACCEPT, new ProviderProposal());

        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, operation, provider));
    }

    @DisplayName("A transaction awaiting the response of requester changes it's status to 'accepted by requester'")
    @Test
    void requesterAcceptsTransactionTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_REQUESTER_CONFIRMATION;
        TransactionOperation operation = new TransactionOperation(TransactionResponse.ACCEPT, new ProviderProposal());


        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, operation, requester);

        verify(transaction).setStatus(TransactionStatus.ACCEPTED_BY_REQUESTER);

    }

    @DisplayName("A transaction awaiting the response of requester changes it's status to 'declined by requester'")
    @Test
    void requesterDeclinesTransactionTest() throws TransactionStatusHandlerException, IllegalTransactionOperationException {
        TransactionStatus status = TransactionStatus.AWAITING_REQUESTER_CONFIRMATION;
        TransactionOperation operation = new TransactionOperation(TransactionResponse.DECLINE, new ProviderProposal());

        TransactionStatusHandler
                .getHandlerFor(status)
                .handleTransaction(transaction, operation, requester);

        verify(transaction).setStatus(TransactionStatus.DECLINED_BY_REQUESTER);

    }

    @DisplayName("A transaction with a 'declined by provider' status cannot change it's status")
    @Test
    void declinedTransactionCannotChangeItsStatusTest() {
        TransactionStatus status = TransactionStatus.DECLINED_BY_PROVIDER;
        TransactionOperation operation = new TransactionOperation(TransactionResponse.ACCEPT, new ProviderProposal());


        assertThrows(
                IllegalTransactionOperationException.class,
                () -> TransactionStatusHandler
                        .getHandlerFor(status)
                        .handleTransaction(transaction, operation, provider));
    }

}
