package com.changas.model.QuestionsAndAnswers;

import com.changas.model.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class QuestionAndAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDate date;
    private Customer customer;

    public QuestionAndAnswer(String message,LocalDate date,Customer customer){
        this.message = message;
        this.date = date;
        this.customer = customer;

    }}
