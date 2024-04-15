package com.changas.dto.changa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HireChangaRequest(
        @JsonProperty(value = "changa_id")
        Long changaId
) {
}
