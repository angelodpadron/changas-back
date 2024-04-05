package com.changas.dto;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HiringOverviewDTO {
    private String hiringId;
    private String changaId;
    private String changaTitle;
    private String changaDescription;
    private String changaPhotoUrl;
    private Instant creationDate;
}
