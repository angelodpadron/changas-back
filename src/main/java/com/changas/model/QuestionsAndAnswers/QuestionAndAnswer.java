package com.changas.model.QuestionsAndAnswers;

import com.changas.model.Changa;
import com.changas.model.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class QuestionAndAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changa_id", nullable = false)
    private Changa changa;

    public QuestionAndAnswer(String message, LocalDate date, Customer customer,Changa changa){
        this.message = message;
        this.date = date;
        this.customer = customer;
        this.changa = changa;
    }
}