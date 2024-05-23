package com.changas.model;

import com.changas.exceptions.changa.UnauthorizedChangaEditException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChangaTest {

    @Test
    @DisplayName("A changa is available to request when created")
    void changaIsAvailableWhenCreatedTest() {
        Customer provider = mock(Customer.class);
        Changa changa = new Changa("title", "description", "photo_url", new HashSet<>(), provider);
        assertTrue(changa.getAvailable());
    }

    @Test
    @DisplayName("A changa can be deactivated by provider")
    void changaDeactivationTest() throws UnauthorizedChangaEditException {
        Customer provider = mock(Customer.class);
        when(provider.getId()).thenReturn(1L);

        Changa changa = new Changa("title", "description", "photo_url", new HashSet<>(), provider);

        changa.deactivateAs(provider);

        assertFalse(changa.getAvailable());
    }

    @Test
    @DisplayName("A changa cannot be deactivated if not by provider")
    void changaDeactivationExceptionTest()  {
        Customer provider = mock(Customer.class);
        Customer randomCustomer = mock(Customer.class);
        when(provider.getId()).thenReturn(1L);
        when(randomCustomer.getId()).thenReturn(2L);

        Changa changa = new Changa("title", "description", "photo_url", new HashSet<>(), provider);

        assertThrows(UnauthorizedChangaEditException.class, () ->changa.deactivateAs(randomCustomer));
    }

}
