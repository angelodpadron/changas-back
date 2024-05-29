package com.changas.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateReviewRequest {
    @JsonProperty(value = "changa_id")
    private Long changaId;
    private int rating;
    private String comment;
    @JsonProperty(value = "photo_url")
    private String photoUrl;
}
