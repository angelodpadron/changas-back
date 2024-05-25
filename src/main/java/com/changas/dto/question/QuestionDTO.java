package com.changas.dto.question;

import com.changas.dto.customer.CustomerOverviewDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class QuestionDTO {

    private String message;
    private LocalDate date;
    private CustomerOverviewDTO customer;
}
