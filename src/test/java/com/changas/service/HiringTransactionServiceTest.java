package com.changas.service;

import com.changas.dto.hiring.HireChangaRequest;
import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.ProviderProposalDTO;
import com.changas.dto.hiring.WorkAreaDetailsDTO;
import com.changas.dto.hiring.response.HiringResponse;
import com.changas.exceptions.HiringOwnChangaException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.hiring.HiringTransactionNotFoundException;
import com.changas.exceptions.status.IllegalTransactionOperationException;
import com.changas.exceptions.status.TransactionStatusHandlerException;
import com.changas.model.*;
import com.changas.model.status.TransactionOperation;
import com.changas.model.status.TransactionResponse;
import com.changas.model.status.TransactionStatus;
import com.changas.model.status.TransactionStatusHandler;
import com.changas.model.status.handlers.AcceptedByRequesterHandler;
import com.changas.model.status.handlers.AwaitingProviderConfirmationHandler;
import com.changas.model.status.handlers.AwaitingRequesterConfirmationHandler;
import com.changas.repository.HiringTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class HiringTransactionServiceTest {

    @Mock
    HiringTransactionRepository hiringTransactionRepository;
    @Mock
    AuthService authService;
    @Mock
    ChangaService changaService;

    @InjectMocks
    private HiringTransactionService hiringTransactionService;

    private Customer customer;
    private Customer provider;
    private Changa changa;
    private WorkAreaDetails workAreaDetails;
    private ProviderProposal providerProposal;
    private final WorkAreaDetailsDTO workAreaDetailsDTO = new WorkAreaDetailsDTO(null,"photo_url", "work_area_description");
    private final ProviderProposalDTO providerProposalDTO = new ProviderProposalDTO(1L,"message", BigDecimal.ONE);

    @BeforeEach
    void setUp() {
        this.customer = mock(Customer.class);
        this.provider = mock(Customer.class);
        this.changa = mock(Changa.class);
        this.workAreaDetails = mock(WorkAreaDetails.class);
        this.providerProposal = mock(ProviderProposal.class);
    }

    @Test
    @DisplayName("A hiring request generates a hiring transaction overview with an awaiting provider confirmation status")
    void generateHiringTransactionOverview() throws HiringOwnChangaException, CustomerNotAuthenticatedException, ChangaNotFoundException {
        when(customer.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(provider);  // The changa is not created by the customer interested in hiring
        when(authService.getCustomerAuthenticated()).thenReturn(customer);
        when(changaService.getChangaById(changa.getId())).thenReturn(changa);

        HireChangaRequest hireChangaRequest = new HireChangaRequest(changa.getId(), this.workAreaDetailsDTO);
        HiringOverviewDTO hiringOverviewDTO = hiringTransactionService.requestChanga(hireChangaRequest);

        assertNotNull(hiringOverviewDTO);
        assertEquals(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION, hiringOverviewDTO.getStatus());

    }

    @Test
    @DisplayName("Hiring an owned changa throws an exception")
    void hiringOwnChangaExceptionTest() throws CustomerNotAuthenticatedException, ChangaNotFoundException {
        when(customer.getId()).thenReturn(1L);
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(customer);  // The changa is created by the customer interested in hiring
        when(authService.getCustomerAuthenticated()).thenReturn(customer);
        when(changaService.getChangaById(changa.getId())).thenReturn(changa);

        HireChangaRequest hireChangaRequest = new HireChangaRequest(changa.getId(), this.workAreaDetailsDTO);

        assertThrows(HiringOwnChangaException.class, () -> hiringTransactionService.requestChanga(hireChangaRequest));
    }

    @Test
    @DisplayName("Hiring a unavailable changa throws an exception")
    void hiringAnUnavailableChangaTest() throws CustomerNotAuthenticatedException, ChangaNotFoundException {
        when(customer.getId()).thenReturn(1L);
        when(authService.getCustomerAuthenticated()).thenReturn(customer);
        when(changaService.getChangaById(any())).thenThrow(ChangaNotFoundException.class);

        HireChangaRequest hireChangaRequest = new HireChangaRequest(1L, this.workAreaDetailsDTO);

        assertThrows(ChangaNotFoundException.class, () -> hiringTransactionService.requestChanga(hireChangaRequest));
    }

    @Test
    @DisplayName("Hiring a changa without being authenticated throws an exception")
    void hiringAChangaWithoutAuthTest() throws CustomerNotAuthenticatedException {

        HireChangaRequest hireChangaRequest = new HireChangaRequest(1L, this.workAreaDetailsDTO);

        when(authService.getCustomerAuthenticated()).thenThrow(CustomerNotAuthenticatedException.class);

        assertThrows(CustomerNotAuthenticatedException.class, () -> hiringTransactionService.requestChanga(hireChangaRequest));
    }


    @DisplayName("Provider respond accept a changa request")
    @Test
    void respondAChangaRequestTest() throws CustomerNotAuthenticatedException, HiringOwnChangaException, TransactionStatusHandlerException, IllegalTransactionOperationException, HiringTransactionNotFoundException {
        TransactionResponse transactionResponse = TransactionResponse.ACCEPT; //provider acepta la solicitud
        when(customer.getId()).thenReturn(1L); //id cliente
        when(provider.getId()).thenReturn(2L);  //id proveedor de la changa
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(provider);

        HiringTransaction transaction = HiringTransaction.generateTransactionFor(changa,customer,workAreaDetails);
        HiringResponse hiringResponse = new HiringResponse(transaction.getId(),transactionResponse,providerProposalDTO); //se crea la respuesta para el cliente

        when(authService.getCustomerAuthenticated()).thenReturn(customer);
        when(hiringTransactionRepository.findById(hiringResponse.getTransactionId())).thenReturn(Optional.ofNullable(transaction));

        TransactionOperation operation = new TransactionOperation(hiringResponse.getResponse(),providerProposal);

        TransactionStatusHandler handler =
                AwaitingProviderConfirmationHandler.getHandlerFor(transaction.getStatus());

        assertEquals(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION, transaction.getStatus());
        handler.handleTransaction(transaction,operation,provider);
        HiringOverviewDTO hiringOverviewDTO = hiringTransactionService.respondChangaRequest(hiringResponse);

        assertEquals(TransactionStatus.ACCEPTED_BY_REQUESTER, transaction.getStatus());
        assertEquals(TransactionStatus.ACCEPTED_BY_REQUESTER, hiringOverviewDTO.getStatus());
    }

    @Test
    @DisplayName("get all transactions from customer authenticated")
    public void getAllTransactionsFromCustomerAuthenticatedTest() throws CustomerNotAuthenticatedException, HiringOwnChangaException {
        when(authService.getCustomerAuthenticated()).thenReturn(customer);
        when(customer.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(customer);
        HiringTransaction transaction = HiringTransaction.generateTransactionFor(changa,provider,workAreaDetails);
        Set<HiringTransaction> ts = new HashSet<>();
        ts.add(transaction);

        when( hiringTransactionRepository.allTransactionsFromCustomer(customer.getId())).thenReturn(ts);
        List<HiringOverviewDTO> ret = hiringTransactionService.getTransactionsFromCustomer();

        assertEquals(ret.size(),ts.size());
    }

    @Test
    @DisplayName("Find transaction from customer id")
    public void findTransactionFromCustomerIdTest() throws HiringOwnChangaException, HiringTransactionNotFoundException, CustomerNotAuthenticatedException {
        when(authService.getCustomerAuthenticated()).thenReturn(customer);
        when(customer.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(provider);

        HiringTransaction transaction = HiringTransaction.generateTransactionFor(changa,customer,workAreaDetails);
        when( hiringTransactionRepository.findCustomerTransactionById(transaction.getId(),customer.getId())).thenReturn(Optional.of(transaction));

        HiringOverviewDTO hiringOverviewDTO = hiringTransactionService.getHiringOverviewFromCustomer(transaction.getId());

        assertNotNull(hiringOverviewDTO);
    }

    @Test
    @DisplayName("Get Transactions from customer with Status Decline")
    public void getAllTransactionsFromCustomerWithStatusDeclineTest() throws HiringOwnChangaException, CustomerNotAuthenticatedException, TransactionStatusHandlerException, IllegalTransactionOperationException {
        when(customer.getId()).thenReturn(1L); //id cliente
        when(provider.getId()).thenReturn(2L);  //id proveedor de la changa
        when(changa.getId()).thenReturn(1L);
        when(changa.getProvider()).thenReturn(provider);
        when(authService.getCustomerAuthenticated()).thenReturn(customer);

        HiringTransaction transaction = HiringTransaction.generateTransactionFor(changa,customer,workAreaDetails);
        TransactionResponse transactionResponse = TransactionResponse.DECLINE;

        HiringResponse hiringResponse = new HiringResponse(transaction.getId(),transactionResponse,providerProposalDTO);

        when(hiringTransactionRepository.findById(hiringResponse.getTransactionId())).thenReturn(Optional.ofNullable(transaction));

        TransactionOperation operation = new TransactionOperation(hiringResponse.getResponse(),providerProposal);

        TransactionStatusHandler handler =
                AwaitingProviderConfirmationHandler.getHandlerFor(transaction.getStatus());

        assertEquals(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION, transaction.getStatus());
        handler.handleTransaction(transaction,operation,provider);

        Set<HiringTransaction> ts = new HashSet<>();
        ts.add(transaction);
        when(hiringTransactionRepository.findByCustomerIdAndStatus(customer.getId(),transaction.getStatus())).thenReturn(ts);
        List<HiringOverviewDTO> transactions = hiringTransactionService.getTransactionsFromCustomerWithStatus(TransactionStatus.DECLINED_BY_PROVIDER);

        assertEquals(TransactionStatus.DECLINED_BY_PROVIDER, transaction.getStatus());
        assertEquals(transactions.size(),1);

    }
}