package com.changas.dto;

import java.util.List;

public record ApiError(
        String message,
        List<String> details
) {
}
