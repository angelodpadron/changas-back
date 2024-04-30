package com.changas.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CustomerTest {

    @Test
    @DisplayName("A user initially has no changas")
    void aUserInitiallyHasNoChangasTest() {
        Customer customer = new Customer();
        assertEquals(0, customer.getPosts().size());
    }

//    @Test
//    @DisplayName("A user initially has no hirings")
//    void aUserInitiallyHasNoHiringsTest() {
//        Customer customer = new Customer();
//        assertEquals(0, customer.getTransactions().size());
//    }
//
//    @Test
//    @DisplayName("A user can save a hiring transaction")
//    void aUserCanHireAChangaTest() {
//        Customer customer = new Customer();
//        HiringTransaction hiringTransaction = mock(HiringTransaction.class);
//
//        customer.saveHiringTransaction(hiringTransaction);
//
//        assertTrue(customer.getTransactions().contains(hiringTransaction));
//    }

    @Test
    @DisplayName("A user can save a changa")
    void aUserCanCreateAChangaTest() {
        Customer customer = new Customer();
        Changa changa = mock(Changa.class);

        customer.saveChangaPost(changa);

        assertTrue(customer.getPosts().contains(changa));
    }

}
