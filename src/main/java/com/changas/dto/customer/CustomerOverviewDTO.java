package com.changas.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CustomerOverviewDTO {
    private Long id;
    private String name;
    private String email;
    @JsonProperty(value = "photo_url")
    private String photoUrl;
}
