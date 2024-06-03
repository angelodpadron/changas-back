package com.changas.dto.question;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class QuestionDTO {

    private Long id;
    private String message;
    private LocalDate date;
    @JsonProperty(value = "customer_name")
    private String customer_name;
    @JsonProperty(value = "changa_id")
    private Long changa_id;
}
