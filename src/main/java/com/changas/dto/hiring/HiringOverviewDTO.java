package com.changas.dto.hiring;

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
    private String changaTitle;
    private String changaDescription;
    private String changaPhotoUrl;
    private Instant creationDate;
}
