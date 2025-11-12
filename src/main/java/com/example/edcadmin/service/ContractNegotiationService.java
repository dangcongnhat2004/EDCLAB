package com.example.edcadmin.service;

import com.example.edcadmin.repository.EdcContractNegotiationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ContractNegotiationService {

    private final EdcContractNegotiationRepository contractNegotiationRepository;

    public ContractNegotiationService(EdcContractNegotiationRepository contractNegotiationRepository) {
        this.contractNegotiationRepository = contractNegotiationRepository;
    }

    public Mono<String> negotiate(String providerUrl, String contractOfferId, String assetId) {
        return contractNegotiationRepository.negotiate(providerUrl, contractOfferId, assetId);
    }
}

