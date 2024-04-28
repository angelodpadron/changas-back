package com.changas.dto.changa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record CreateChangaRequest(
        String title,
        String description,
        @JsonProperty(value = "photo_url")

        String photoUrl,
        Set<String> topics
) {
}
