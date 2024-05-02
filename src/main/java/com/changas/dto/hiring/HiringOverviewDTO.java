package com.changas.dto.hiring;

import com.changas.model.status.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HiringOverviewDTO {
    @JsonProperty(value = "hiring_id")
    private Long hiringId;
    @JsonProperty(value = "changa_id")
    private Long changaId;
    @JsonProperty(value = "provider_id")
    private Long providerId;
    @JsonProperty(value = "customer_id")
    private Long customerId;
    @JsonProperty(value = "changa_title")
    private String changaTitle;
    @JsonProperty(value = "changa_description")
    private String changaDescription;
    @JsonProperty(value = "changa_photo_url")
    private String changaPhotoUrl;
    @JsonProperty(value = "creation_date")
    private Instant creationDate;
    @JsonProperty(value = "work_details")
    private String workDetails;
    @JsonProperty(value = "work_area_photo_url")
    private String workAreaPhotoUrl;
    private TransactionStatus status;
}
