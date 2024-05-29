package com.changas.dto.hiring;

import java.math.BigDecimal;

public record ProviderProposalDTO(
        Long id,
        String message,
        BigDecimal price
) {
}
