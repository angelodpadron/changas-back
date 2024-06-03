package com.changas.service;

import com.changas.dto.question.QuestionDTO;
import com.changas.exceptions.QuestionAndAnswer.QuestionNotFoundException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.QuestionsAndAnswers.Question;
import com.changas.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.changas.mappers.QuestionMapper.toQuestionDTO;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AuthService authService;
    private final ChangaService changaService;

    @Transactional
    public QuestionDTO createQuestion(Long changa_id,String message) throws CustomerNotAuthenticatedException, ChangaNotFoundException {
        Customer customer = authService.getCustomerAuthenticated();
        Changa changa = changaService.getChangaById(changa_id);
        Question question = new Question(message, LocalDate.now(), customer, changa);

        questionRepository.save(question);

        return toQuestionDTO(question);
    }

    public QuestionDTO getQuestionById(Long questionId) throws QuestionNotFoundException {
        return toQuestionDTO(questionRepository.findById(questionId).orElseThrow(()-> new QuestionNotFoundException(questionId)));
    }

    public List<QuestionDTO> getAllQuestions() {
        List<QuestionDTO> qs = new ArrayList<>();
        questionRepository.findAll().forEach(question -> qs.add(toQuestionDTO(question)));
        return qs;
    }
}
