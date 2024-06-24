package com.changas.dto.changa;

import com.changas.dto.area.ServiceAreaDTO;
import com.changas.dto.customer.CustomerOverviewDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
public class ChangaOverviewDTO {
    private Long id;
    private String title;
    private String description;
    @JsonProperty(value = "photo_url")
    private String photoUrl;
    private Set<String> topics;
    @JsonProperty(value = "provider_summary")
    private CustomerOverviewDTO customerSummary;
    @JsonProperty(value = "service_area")
    private ServiceAreaDTO serviceAreaDTO;
    private boolean available;
    @JsonProperty(value = "created_at")
    private Instant createdAt;
    @JsonProperty(value = "last_update")
    private Instant lastUpdate;
}
