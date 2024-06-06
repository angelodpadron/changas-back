package com.changas.exceptions.inquiry;

public class QuestionNotFoundException extends InquiryException {
    public QuestionNotFoundException(Long question_id){super ("No question found with id: " + question_id);}
}
