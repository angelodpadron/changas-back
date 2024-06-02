package com.changas.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReviewRequest {
    @NotNull(message = "Changa ID is required")
    @JsonProperty(value = "changa_id")
    private Long changaId;
    @Min(value = 1, message = "Rating should be greater than or equal than 1")
    @Max(value = 5, message = "Rating should be lesser or equal than 5")
    private int rating;
    @Size(max = 500, message = "Comment cannot have more that 500 characters")
    private String comment;
    @JsonProperty(value = "photo_url")
    private String photoUrl;
}
