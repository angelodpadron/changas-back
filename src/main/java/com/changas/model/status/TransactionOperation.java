package com.changas.model.status;

import com.changas.model.ProviderProposal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
public class TransactionOperation {
    @Getter
    private TransactionResponse response;
    private ProviderProposal providerProposal;

    public Optional<ProviderProposal> getProviderProposal() {
        return Optional.ofNullable(providerProposal);
    }
}
