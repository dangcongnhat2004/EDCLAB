package com.example.edcadmin.controller;

import com.example.edcadmin.model.contract.ContractDefinitionCreateRequest;
import com.example.edcadmin.service.ContractDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
public class ContractDefinitionController {

    private final ContractDefinitionService contractDefinitionService;

    public ContractDefinitionController(ContractDefinitionService contractDefinitionService) {
        this.contractDefinitionService = contractDefinitionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> create(@Valid @RequestBody ContractDefinitionCreateRequest req) {
        return contractDefinitionService.create(req.getId(), req.getPolicyId());
    }

    @GetMapping(value = "/edc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> listOnEdc() {
        return contractDefinitionService.findAll();
    }
}
