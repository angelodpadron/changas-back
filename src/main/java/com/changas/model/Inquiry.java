package com.changas.model;

import com.changas.exceptions.inquiry.InquiryException;
import com.changas.exceptions.inquiry.QuestionAlreadyAnsweredException;
import com.changas.exceptions.inquiry.SelfQuestionException;
import com.changas.exceptions.inquiry.UnauthorizedAnswerException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String answer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changa_id", nullable = false)
    private Changa changa;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    private Instant lastUpdate;


    public static Inquiry generateFor(String question, Customer customer, Changa changa) throws SelfQuestionException {
        checkIfCanAsk(customer, changa);
        return Inquiry
                .builder()
                .question(question)
                .customer(customer)
                .changa(changa)
                .build();
    }

    private static void checkIfCanAsk(Customer customer, Changa changa) throws SelfQuestionException {
        if (customer.getId().equals(changa.getProvider().getId())) {
            throw new SelfQuestionException();
        }
    }

    public void answer(Customer customer, String answer) throws InquiryException {
        checkIfCanRespond(customer);
        setAnswer(answer);
    }

    private void checkIfCanRespond(Customer customer) throws InquiryException {
        if (!customer.getId().equals(changa.getProvider().getId())) {
            throw new UnauthorizedAnswerException();
        }

        if (answer != null) {
            throw new QuestionAlreadyAnsweredException();
        }
    }
}