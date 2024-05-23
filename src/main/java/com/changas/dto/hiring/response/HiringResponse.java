package com.changas.dto.hiring.response;

import com.changas.dto.hiring.ProviderProposalDTO;
import com.changas.model.status.TransactionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter
public class HiringResponse {
    @Getter
    @JsonProperty(value = "transaction_id")
    private Long transactionId;
    @Getter
    private TransactionResponse response;
    @JsonProperty(value = "provider_proposal")
    private ProviderProposalDTO providerProposal;

    public Optional<ProviderProposalDTO> getProviderProposal() {
        return Optional.ofNullable(providerProposal);
    }
}
