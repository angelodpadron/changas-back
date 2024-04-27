package com.changas.dto.changa;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ChangaOverviewDTO {
    private Long id;
    private String title;
    private String description;
    private String photoUrl;
    private List<String> topics;
    @JsonProperty("providerSummary")
    private CustomerOverviewDTO customerSummary;
}
