package com.changas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CustomerOverviewDTO {
    private Long id;
    private String name;
    private String email;
    private String photoUrl;
}
