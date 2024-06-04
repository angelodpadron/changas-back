package com.changas.dto.review;

import com.changas.dto.customer.CustomerOverviewDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;
    @JsonProperty(value = "photo_url")
    private String photoUrl;
    @JsonProperty(value = "created_at")
    private Instant createdAt;
    private CustomerOverviewDTO reviewer;

}
