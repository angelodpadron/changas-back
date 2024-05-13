package com.changas.dto.changa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.util.Optional;
import java.util.Set;

@Setter
public class UpdateChangaRequest {
    private String title;
    private String description;
    @JsonProperty(value = "photo_url")
    private String photoUrl;
    private Set<String> topics;

    public Optional<String> getTitle() {
        if (title != null) return Optional.of(title);
        return Optional.empty();
    }

    public Optional<String> getDescription() {
        if (description != null) return Optional.of(description);
        return Optional.empty();
    }

    public Optional<String> getPhotoUrl() {
        if (photoUrl != null) return Optional.of(photoUrl);
        return Optional.empty();
    }

    public Optional<Set<String>> getTopics() {
        if (topics != null) return Optional.of(topics);
        return Optional.empty();
    }
}
