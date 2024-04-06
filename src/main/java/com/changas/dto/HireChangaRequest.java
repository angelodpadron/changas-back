package com.changas.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HireChangaRequest {
    private String changaId;
    private String customerId;
}
