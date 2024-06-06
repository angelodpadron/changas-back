package com.changas.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateQuestionRequest(
        @JsonProperty(value = "changa_id")
        Long changaId,
        String question
) {
}
