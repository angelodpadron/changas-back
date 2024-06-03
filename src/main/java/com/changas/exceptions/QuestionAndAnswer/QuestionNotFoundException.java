package com.changas.exceptions.QuestionAndAnswer;

public class QuestionNotFoundException extends Exception{
    public QuestionNotFoundException(Long question_id){super ("No question found with id: " + question_id);}
}
