package com.changas.dto.hiring;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HiringOverviewDTO {
    private Long hiringId;
    private Long changaId;
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
}
