package com.changas.model.QuestionsAndAnswers;

import com.changas.model.Changa;
import com.changas.model.Customer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Question extends QuestionAndAnswer{

    public Question(String message, LocalDate date, Customer customer, Changa changa){
        super(message,date,customer,changa);
    }
}
