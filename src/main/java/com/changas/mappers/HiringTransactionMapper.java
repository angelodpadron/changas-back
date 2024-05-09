package com.changas.mappers;

import com.changas.dto.hiring.HiringOverviewDTO;
import com.changas.dto.hiring.ProviderProposalDTO;
import com.changas.dto.hiring.WorkAreaDetailsDTO;
import com.changas.model.HiringTransaction;
import com.changas.model.ProviderProposal;
import com.changas.model.WorkAreaDetails;

public class HiringTransactionMapper {

    public static HiringOverviewDTO toHiringOverviewDTO(HiringTransaction transaction) {

        return HiringOverviewDTO
                .builder()
                .hiringId(transaction.getId())
                .providerId(transaction.getId())
                .customerId(transaction.getRequester().getId())
                .creationDate(transaction.getCreationDate())
                .status(transaction.getStatus())
                .changaTitle(transaction.getChanga().getTitle())
                .changaDescription(transaction.getChanga().getDescription())
                .changaPhotoUrl(transaction.getChanga().getPhotoUrl())
                .workAreaDetailsDTO(asWorkAreaDetailsDTO(transaction.getWorkAreaDetails()))
                .providerProposalDTO(asProviderProposalDTO(transaction.getProviderProposal()))
                .build();
    }

    private static WorkAreaDetailsDTO asWorkAreaDetailsDTO(WorkAreaDetails details) {
        return new WorkAreaDetailsDTO(details.getPhotoUrl(), details.getDescription());
    }

    private static ProviderProposalDTO asProviderProposalDTO(ProviderProposal proposal) {
        // TODO: better handling when the hiring request hasn't received a proposal by the provider
        if (proposal == null) {
            return null;
        }
        return new ProviderProposalDTO(proposal.getMessage(), proposal.getPrice());
    }

}
