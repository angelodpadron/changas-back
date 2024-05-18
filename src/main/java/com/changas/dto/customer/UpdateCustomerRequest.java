package com.changas.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateCustomerRequest {
    private String name;
    @JsonProperty(value = "photo_url")
    private String photoUrl;
}
