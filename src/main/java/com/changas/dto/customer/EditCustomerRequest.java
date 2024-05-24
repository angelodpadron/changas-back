package com.changas.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@AllArgsConstructor
@Setter
public class EditCustomerRequest {
    private String name;
    @JsonProperty(value = "photo_url")
    private String photoUrl;

    public Optional<String> getName() {
        return Optional.of(name);
    }

    public Optional<String> getPhotoUrl() {
        return Optional.of(photoUrl);
    }
}
