package com.changas.model;


import com.changas.exceptions.inquiry.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InquiryTest {

    @DisplayName("A customer cannot make inquiries on an own publication")
    @Test
    void customerCannotInquiryOwnedPublicationTest() {
        Changa changa = mock(Changa.class);
        Customer provider = mock(Customer.class);

        when(changa.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(1L);

        assertThrows(SelfQuestionException.class, () -> Inquiry.generateFor("Question", provider, changa));
    }

    @DisplayName("Only the provider can answer inquiries")
    @Test
    void onlyProviderCanAnswerInquiriesTest() throws SelfQuestionException {
        Changa changa = mock(Changa.class);
        Customer provider = mock(Customer.class);
        Customer customer = mock(Customer.class);

        when(changa.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(1L);
        when(customer.getId()).thenReturn(2L);

        Inquiry inquiry = Inquiry.generateFor("Question", customer, changa);

        assertThrows(UnauthorizedAnswerException.class, () -> inquiry.answer(customer, "Answer"));

    }

    @DisplayName("Cannot answer a previously answered inquiry")
    @Test
    void cannotAnswerAnAnsweredInquiryTest() throws InquiryException {
        Changa changa = mock(Changa.class);
        Customer provider = mock(Customer.class);
        Customer customer = mock(Customer.class);

        when(changa.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(1L);
        when(customer.getId()).thenReturn(2L);

        Inquiry inquiry = Inquiry.generateFor("Question", customer, changa);
        inquiry.answer(provider, "Answer");

        assertThrows(QuestionAlreadyAnsweredException.class, () -> inquiry.answer(provider, "Answer"));
    }

    @DisplayName("An inquiry is initially not read")
    @Test
    void inquiryInitiallyIsNotReadTest() throws SelfQuestionException {
        Changa changa = mock(Changa.class);
        Customer provider = mock(Customer.class);
        Customer customer = mock(Customer.class);

        when(changa.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(1L);
        when(customer.getId()).thenReturn(2L);

        Inquiry inquiry = Inquiry.generateFor("Question", customer, changa);

        assertFalse(inquiry.getRead());
    }

    @DisplayName("Cannot mark as read an non answered inquiry")
    @Test
    void cannotMarkAsReadNonAnsweredInquiryTest() throws SelfQuestionException {
        Changa changa = mock(Changa.class);
        Customer provider = mock(Customer.class);
        Customer customer = mock(Customer.class);


        when(changa.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(1L);
        when(customer.getId()).thenReturn(2L);

        Inquiry inquiry = Inquiry.generateFor("Question", customer, changa);

        assertThrows(MarkAsReadException.class, () -> inquiry.markAsRead(customer));

    }

    @DisplayName("Mark answered inquiry as read")
    @Test
    void markAnsweredInquiryAsReadTest() throws InquiryException {
        Changa changa = mock(Changa.class);
        Customer provider = mock(Customer.class);
        Customer customer = mock(Customer.class);

        when(changa.getProvider()).thenReturn(provider);
        when(provider.getId()).thenReturn(1L);
        when(customer.getId()).thenReturn(2L);

        Inquiry inquiry = Inquiry.generateFor("Question", customer, changa);
        inquiry.answer(provider, "Answer");

        inquiry.markAsRead(customer);

        assertTrue(inquiry.getRead());

    }

}
