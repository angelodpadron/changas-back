package com.changas.service;

import com.changas.dto.question.QuestionDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Customer;
import com.changas.model.QuestionsAndAnswers.Question;
import com.changas.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.changas.mappers.QuestionMapper.toQuestionDTO;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AuthService authService;

    @Transactional
    public QuestionDTO createQuestion(String message) throws CustomerNotAuthenticatedException {
        Customer customer = authService.getCustomerAuthenticated();
        Question question = new Question(message, LocalDate.now(),customer);

        questionRepository.save(question);

        return toQuestionDTO(question);
    }
}
