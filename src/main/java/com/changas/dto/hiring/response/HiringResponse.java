package com.changas.dto.hiring.response;

import com.changas.dto.hiring.ProviderProposalDTO;
import com.changas.model.status.TransactionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HiringResponse {
    @JsonProperty(value = "transaction_id")
    private Long transactionId;
    private TransactionResponse response;
    @JsonProperty(value = "provider_proposal")
    private ProviderProposalDTO providerProposal;

}
