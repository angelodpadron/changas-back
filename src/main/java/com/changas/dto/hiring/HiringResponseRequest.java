package com.changas.dto.hiring;

import com.changas.model.status.TransactionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record HiringResponseRequest(
        @JsonProperty(value = "transaction_id")
        Long transactionId,
        TransactionResponse response,
        @JsonProperty(value = "provider_proposal")
        ProviderProposalDTO providerProposal
){}
