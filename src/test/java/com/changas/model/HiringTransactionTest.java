package com.changas.model;

import com.changas.exceptions.HiringOwnChangaException;
import com.changas.model.status.TransactionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HiringTransactionTest {

    @Test
    @DisplayName("A transaction has a awaiting provider response status when generated")
    void generateTransactionTest() throws HiringOwnChangaException {
        Customer requester = mock(Customer.class);
        Customer provider = mock(Customer.class);
        Changa changa = mock(Changa.class);
        WorkAreaDetails workAreaDetails = mock(WorkAreaDetails.class);

        when(changa.getProvider()).thenReturn(provider);
        when(requester.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);

        HiringTransaction hiringTransaction = HiringTransaction.generateTransactionFor(changa, requester, workAreaDetails);

        assertEquals(changa, hiringTransaction.getChanga());
        assertEquals(requester, hiringTransaction.getRequester());
        assertEquals(workAreaDetails, hiringTransaction.getWorkAreaDetails());
        assertEquals(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION, hiringTransaction.getStatus());

    }

    @Test
    @DisplayName("Cannot generate a transaction for an owned changa")
    void generateTransactionForOwnedChangaThrowsExceptionTest() {
        Customer provider = mock(Customer.class);
        Changa changa = mock(Changa.class);
        WorkAreaDetails workAreaDetails = mock(WorkAreaDetails.class);

        when(changa.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(1L);

        assertThrows(HiringOwnChangaException.class, () -> HiringTransaction.generateTransactionFor(changa, provider, workAreaDetails));

    }

}
