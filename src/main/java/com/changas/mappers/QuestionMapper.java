package com.changas.mappers;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.changas.dto.question.QuestionDTO;
import com.changas.model.QuestionsAndAnswers.Question;

public class QuestionMapper {
    public static QuestionDTO toQuestionDTO(Question question){
        return QuestionDTO.builder()
                .message(question.getMessage())
                .date(question.getDate())
                .customer(CustomerOverviewDTO.builder()
                        .id(question.getCustomer().getId())
                        .name(question.getCustomer().getName())
                        .email(question.getCustomer().getEmail())
                        .photoUrl(question.getCustomer().getPhotoUrl())
                        .build())
                .build();
    }
}
