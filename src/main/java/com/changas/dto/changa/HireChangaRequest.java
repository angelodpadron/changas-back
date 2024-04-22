package com.changas.dto.changa;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HireChangaRequest(
        @JsonProperty(value = "changa_id")
        Long changaId,
        @JsonProperty(value = "work_details")
        String workDetails,
        @JsonProperty(value = "work_area_photo_url")
        String workAreaPhotoUrl
) {
}
