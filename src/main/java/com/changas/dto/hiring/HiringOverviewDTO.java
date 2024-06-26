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
    private Long id;
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
    @JsonProperty(value = "created_at")
    private Instant createdAt;
    @JsonProperty(value = "work_area_details")
    private WorkAreaDetailsDTO workAreaDetailsDTO;
    @JsonProperty(value = "provider_proposal")
    private ProviderProposalDTO providerProposalDTO;
    private TransactionStatus status;
    @JsonProperty(value = "last_update")
    private Instant lastUpdate;

}
