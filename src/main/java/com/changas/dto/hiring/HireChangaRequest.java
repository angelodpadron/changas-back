package com.changas.dto.hiring;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HireChangaRequest(
        @JsonProperty(value = "changa_id")
        Long changaId,
        @JsonProperty(value = "work_area_details")
        WorkAreaDetailsDTO workAreaDetails
) {
}
