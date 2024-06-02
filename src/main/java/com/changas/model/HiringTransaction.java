package com.changas.model;

import com.changas.exceptions.HiringOwnChangaException;
import com.changas.model.status.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@NamedEntityGraph(
        name = "transaction-with-full-details",
        attributeNodes = {
                @NamedAttributeNode("changa"),
                @NamedAttributeNode("requester")
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
                .requester(requester)
                .workAreaDetails(workAreaDetails)
                .status(TransactionStatus.AWAITING_PROVIDER_CONFIRMATION)
                .creationDate(Instant.now())
                .build();
    }

    private static void checkIfCanHire(Customer requester, Changa changa) throws HiringOwnChangaException {
        boolean isProvider = requester.getId().equals(changa.getProvider().getId());
        if (isProvider) throw new HiringOwnChangaException();
    }


}
