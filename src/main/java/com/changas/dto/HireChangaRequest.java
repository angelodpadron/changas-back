package com.changas.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class HireChangaRequest {
    private Long changaId;
    private Long customerId;
}
