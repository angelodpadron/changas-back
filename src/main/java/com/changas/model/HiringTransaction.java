package com.changas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class HiringTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "changa_id", nullable = false)
    private Changa changa;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer requester;
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Customer provider;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private String workDetails;
    private String workAreaPhotoUrl;
    private Instant creationDate;

}
