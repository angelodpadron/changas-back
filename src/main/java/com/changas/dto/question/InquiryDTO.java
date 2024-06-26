package com.changas.dto.question;

import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class InquiryDTO {
    private Long id;
    private String question;
    private String answer;
    @JsonProperty(value = "changa_id")
    private Long changaId;
    private ChangaOverviewDTO changa;
    private CustomerOverviewDTO customer;
    @JsonProperty(value = "created_at")
    private Instant createdAt;
    @JsonProperty(value = "last_update")
    private Instant lastUpdate;
}
