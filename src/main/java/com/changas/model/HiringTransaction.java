package com.changas.model;

import com.changas.exceptions.HiringOwnChangaException;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "work_area_details_id", nullable = false)
    private WorkAreaDetails workAreaDetails;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "provider_proposal_id")
    private ProviderProposal providerProposal;

    private Instant creationDate;

    public static HiringTransaction generateTransactionFor(Changa changa, Customer requester, WorkAreaDetails workAreaDetails) throws HiringOwnChangaException {
        checkIfCanHire(requester, changa);
        return HiringTransaction
                .builder()
                .changa(changa)
                .provider(changa.getProvider())
                .requester(requester)
                .workAreaDetails(workAreaDetails)
                .status(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION)
                .creationDate(Instant.now())
                .build();
    }

    private static void checkIfCanHire(Customer requester, Changa changa) throws HiringOwnChangaException {
        if (requester.getId().equals(changa.getProvider().getId())) {
            throw new HiringOwnChangaException();
        }
    }



}
