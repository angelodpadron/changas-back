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
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    private Instant createdAt;
    private Boolean isRead = false;
    @ManyToOne
    @JoinColumn(name = "changa_id", nullable = false)
    private Changa changa;

}
