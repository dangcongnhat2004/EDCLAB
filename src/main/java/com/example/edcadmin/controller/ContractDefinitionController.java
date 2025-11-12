package com.example.edcadmin.controller;

import com.example.edcadmin.model.ContractDefinitionCreateRequest;
import com.example.edcadmin.service.EdcService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/contracts")
@CrossOrigin(origins = "*")
public class ContractDefinitionController {

    private final EdcService edc;

    public ContractDefinitionController(EdcService edc) { this.edc = edc; }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> create(@Valid @RequestBody ContractDefinitionCreateRequest req) {
        return edc.createContractDef(req.id(), req.policyId());
    }

    @GetMapping(value = "/edc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> listOnEdc() {
        return edc.listContractDefsEdc();
    }
}
