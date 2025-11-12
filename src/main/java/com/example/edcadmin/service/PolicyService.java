package com.example.edcadmin.service;

import com.example.edcadmin.repository.EdcPolicyRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PolicyService {

    private final EdcPolicyRepository policyRepository;

    public PolicyService(EdcPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public Mono<String> create(String policyId) {
        return policyRepository.create(policyId);
    }

    public Mono<String> findAll() {
        return policyRepository.findAll();
    }
}

