package com.changas.service;

import com.changas.dto.question.InquiryDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.inquiry.InquiryException;
import com.changas.exceptions.inquiry.QuestionNotFoundException;
import com.changas.exceptions.inquiry.SelfQuestionException;
import com.changas.mappers.InquiryMapper;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.Inquiry;
import com.changas.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.changas.mappers.InquiryMapper.toInquiryDTO;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final AuthService authService;
    private final ChangaService changaService;

    @Transactional
    public InquiryDTO createQuestion(Long changaId, String question) throws CustomerNotAuthenticatedException, ChangaNotFoundException, SelfQuestionException {
        Customer customer = authService.getCustomerAuthenticated();
        Changa changa = changaService.getChangaById(changaId);

        Inquiry inquiry = Inquiry.generateFor(question, customer, changa);

        inquiryRepository.save(inquiry);

        return toInquiryDTO(inquiry);
    }

    @Transactional
    public InquiryDTO answerQuestion(Long questionId, String response) throws CustomerNotAuthenticatedException, InquiryException {
        Customer provider = authService.getCustomerAuthenticated();
        Inquiry inquiry = inquiryRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));

        inquiry.answer(provider, response);

        inquiryRepository.save(inquiry);

        return toInquiryDTO(inquiry);
    }

    public InquiryDTO getQuestionById(Long questionId) throws InquiryException {
        return inquiryRepository
                .findById(questionId)
                .map(InquiryMapper::toInquiryDTO)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
    }

    public List<InquiryDTO> getAllQuestions(Long changaId) {
        return inquiryRepository
                .findByChangaId(changaId)
                .stream()
                .map(InquiryMapper::toInquiryDTO)
                .collect(Collectors.toList());
    }

    public List<InquiryDTO> getPendingInquiries() throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        return inquiryRepository
                .getPendingInquiriesFor(customer.getId())
                .stream()
                .map(InquiryMapper::toInquiryDTO)
                .collect(Collectors.toList());
    }
}
