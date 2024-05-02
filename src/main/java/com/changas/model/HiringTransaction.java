package com.changas.model;

import com.changas.model.status.TransactionStatus;
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
@NamedEntityGraph(
        name = "transaction-with-full-details",
        attributeNodes = {
                @NamedAttributeNode("changa"),
                @NamedAttributeNode("requester"),
                @NamedAttributeNode("provider")
        }
)
public class HiringTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changa_id", nullable = false)
    private Changa changa;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer requester;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Customer provider;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private String workDetails;
    private String workAreaPhotoUrl;
    private Instant creationDate;

}
