package com.changas.model;

import com.changas.exceptions.inquiry.*;
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
    private Boolean read;
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
                .read(false)
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

    public void markAsRead(Customer customer) throws MarkAsReadException {
        checkIfCanMark(customer);
        read = true;
    }

    private void checkIfCanMark(Customer customer) throws MarkAsReadException {
        boolean isCustomer = customer.getId().equals(this.customer.getId());
        boolean isAnswered = answer != null;

        if (!(isCustomer && isAnswered)) throw new MarkAsReadException();
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