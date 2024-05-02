package com.changas.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

public class Changa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String photoUrl;
    @ElementCollection
    private Set<String> topics;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer provider;
}
