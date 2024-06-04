package com.changas.dto.hiring;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WorkAreaDetailsDTO (
        Long id,
        @JsonProperty(value = "photo_url")
        String photoUrl,
        String description
) {
}
