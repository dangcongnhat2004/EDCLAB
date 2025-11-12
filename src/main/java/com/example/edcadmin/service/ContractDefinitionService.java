package com.example.edcadmin.service;

import com.example.edcadmin.repository.EdcContractDefinitionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ContractDefinitionService {

    private final EdcContractDefinitionRepository contractDefinitionRepository;

    public ContractDefinitionService(EdcContractDefinitionRepository contractDefinitionRepository) {
        this.contractDefinitionRepository = contractDefinitionRepository;
    }

    public Mono<String> create(String id, String policyId) {
        return contractDefinitionRepository.create(id, policyId);
    }

    public Mono<String> findAll() {
        return contractDefinitionRepository.findAll();
    }
}

