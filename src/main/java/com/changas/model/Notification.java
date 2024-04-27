package com.changas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;
    private Instant createdAt;
    private Boolean isRead = false;
    @ManyToOne
    private Changa changa;

}
