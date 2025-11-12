package com.example.edcadmin.controller;

import com.example.edcadmin.model.ContractNegotiationRequest;
import com.example.edcadmin.service.EdcService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/contract-negotiations")
public class ContractNegotiationController {

    private final EdcService edc;

    public ContractNegotiationController(EdcService edc) { this.edc = edc; }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> negotiate(@Valid @RequestBody ContractNegotiationRequest req) {
        return edc.negotiateContract(req.providerUrl(), req.contractOfferId(), req.assetId());
    }
}

