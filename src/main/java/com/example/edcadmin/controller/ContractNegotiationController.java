package com.example.edcadmin.controller;

import com.example.edcadmin.model.contract.ContractNegotiationRequest;
import com.example.edcadmin.service.ContractNegotiationService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/contract-negotiations")
public class ContractNegotiationController {

    private final ContractNegotiationService contractNegotiationService;

    public ContractNegotiationController(ContractNegotiationService contractNegotiationService) {
        this.contractNegotiationService = contractNegotiationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> negotiate(@Valid @RequestBody ContractNegotiationRequest req) {
        return contractNegotiationService.negotiate(req.getProviderUrl(), req.getContractOfferId(), req.getAssetId());
    }
}

