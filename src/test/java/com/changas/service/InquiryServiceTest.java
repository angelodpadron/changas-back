package com.changas.service;

import com.changas.dto.question.InquiryDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.inquiry.InquiryException;
import com.changas.exceptions.inquiry.QuestionNotFoundException;
import com.changas.exceptions.inquiry.SelfQuestionException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.Inquiry;
import com.changas.model.ServiceArea;
import com.changas.repository.InquiryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class InquiryServiceTest {

    @Mock
    private AuthService authService;
    @Mock
    private ChangaService changaService;
    @Mock
    private InquiryRepository inquiryRepository;

    @InjectMocks
    private InquiryService inquiryService;

    @DisplayName("Sending a question on a changa post returns an overview of the inquiry")
    @Test
    void createInquiryTest() throws ChangaNotFoundException, CustomerNotAuthenticatedException, SelfQuestionException {
        Changa changa = mock(Changa.class);
        ServiceArea serviceArea = mock(ServiceArea.class);
        Customer customer = mock(Customer.class);
        Customer provider = mock(Customer.class);


        when(customer.getId()).thenReturn(1L);
        when(provider.getId()).thenReturn(2L);

        when(authService.getCustomerAuthenticated()).thenReturn(customer);

        when(changa.getProvider()).thenReturn(provider);
        when(changa.getId()).thenReturn(1L);
        when(changa.getServiceArea()).thenReturn(serviceArea);

        when(changaService.getChangaById(any())).thenReturn(changa);

        InquiryDTO inquiryDTO = inquiryService.createQuestion(1L, "Question");

        verify(inquiryRepository).save(any(Inquiry.class));
        assertEquals("Question", inquiryDTO.getQuestion());
    }

    @DisplayName("Answering a question updates the inquiry and returns an overview of it")
    @Test
    void answerInquiryTest() throws CustomerNotAuthenticatedException, InquiryException {
        Changa changa = mock(Changa.class);
        ServiceArea serviceArea = mock(ServiceArea.class);
        Customer provider = mock(Customer.class);
        Customer customer = mock(Customer.class);

        when(changa.getProvider()).thenReturn(provider);
        when(changa.getId()).thenReturn(1L);
        when(changa.getServiceArea()).thenReturn(serviceArea);

        when(provider.getId()).thenReturn(1L);
        when(customer.getId()).thenReturn(2L);

        Inquiry inquiry = Inquiry.generateFor("Question", customer, changa);

        when(authService.getCustomerAuthenticated()).thenReturn(provider);
        when(inquiryRepository.findById(any())).thenReturn(Optional.of(inquiry));

        InquiryDTO inquiryDTO = inquiryService.answerQuestion(1L, "Answer");

        verify(inquiryRepository).save(inquiry);
        assertEquals("Answer", inquiryDTO.getAnswer());
    }

    @DisplayName("An inquiry overview can be retrieved by id")
    @Test
    void getInquiryByIdTest() throws InquiryException {
        Changa changa = mock(Changa.class);
        ServiceArea serviceArea = mock(ServiceArea.class);
        Customer provider = mock(Customer.class);
        Customer customer = mock(Customer.class);
        Long inquiryId = 1L;

        when(changa.getProvider()).thenReturn(provider);
        when(changa.getId()).thenReturn(1L);
        when(changa.getServiceArea()).thenReturn(serviceArea);

        when(provider.getId()).thenReturn(1L);
        when(customer.getId()).thenReturn(2L);

        Inquiry inquiry = Inquiry.generateFor("Question", customer, changa);
        inquiry.setId(1L);

        when(inquiry.getId()).thenReturn(inquiryId);
        when(inquiryRepository.findById(inquiryId)).thenReturn(Optional.of(inquiry));

        InquiryDTO inquiryDTO = inquiryService.getQuestionById(inquiryId);

        assertEquals(inquiryId, inquiryDTO.getId());
    }

    @DisplayName("Cannot retrieve an inquiry overview with an invalid id")
    @Test
    void getInquiryByIdExceptionTest() {
        when(inquiryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> inquiryService.getQuestionById(1L));
    }

    @DisplayName("Inquiries from a changa can be retrieved by the changa id ")
    @Test
    void getInquiriesFromChangaTest() {
        when(inquiryRepository.findByChangaId(1L)).thenReturn(List.of());

        List<InquiryDTO> inquiries = inquiryService.getAllQuestions(1L);

        assertNotNull(inquiries);
    }

    @DisplayName("Can get inquiries pending of answering by provider")
    @Test
    void pendingInquiriesTest() throws CustomerNotAuthenticatedException {
        Customer provider = mock(Customer.class);
        when(authService.getCustomerAuthenticated()).thenReturn(provider);
        when(inquiryRepository.getPendingInquiriesFor(1L)).thenReturn(List.of());

        List<InquiryDTO> inquiries = inquiryService.getPendingInquiries();

        assertNotNull(inquiries);
    }


}
