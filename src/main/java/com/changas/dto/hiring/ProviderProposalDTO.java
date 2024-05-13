package com.changas.dto.hiring;

import java.math.BigDecimal;

public record ProviderProposalDTO(
        String message,
        BigDecimal price
) {
}
