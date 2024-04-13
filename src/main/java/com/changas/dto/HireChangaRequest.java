package com.changas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public record HireChangaRequest(
        @JsonProperty(value = "changa_id")
        Long changaId,
        @JsonProperty(value = "customer_id")
        Long customerId
) {
}
