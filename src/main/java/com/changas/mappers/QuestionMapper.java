package com.changas.mappers;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.question.QuestionDTO;
import com.changas.model.QuestionsAndAnswers.Question;

public class QuestionMapper {
    public static QuestionDTO toQuestionDTO(Question question){
        return QuestionDTO.builder()
                .id(question.getId())
                .message(question.getMessage())
                .date(question.getDate())
                .customer_name(question.getCustomer().getName())
                .changa_id(question.getChanga().getId())
                .build();
    }
}
