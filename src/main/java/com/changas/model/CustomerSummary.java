package com.changas.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerSummary {
    private String id;
    private String name;
    private String email;
    private String photoUrl;
}
