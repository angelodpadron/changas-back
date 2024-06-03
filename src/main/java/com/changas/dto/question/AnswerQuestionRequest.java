package com.changas.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnswerQuestionRequest(
        @JsonProperty(value = "question_id")
        Long questionId,
        String response
) {
}
