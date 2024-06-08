package com.changas.model;


import com.changas.exceptions.inquiry.InquiryException;
import com.changas.exceptions.inquiry.QuestionAlreadyAnsweredException;
import com.changas.exceptions.inquiry.SelfQuestionException;
import com.changas.exceptions.inquiry.UnauthorizedAnswerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

}
