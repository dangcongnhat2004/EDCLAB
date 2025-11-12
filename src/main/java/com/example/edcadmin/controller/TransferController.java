package com.example.edcadmin.controller;

import com.example.edcadmin.model.TransferRequest;
import com.example.edcadmin.service.EdcService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final EdcService edc;

    public TransferController(EdcService edc) { this.edc = edc; }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> transfer(@Valid @RequestBody TransferRequest req) {
        return edc.transferData(req.contractId(), req.assetId(), req.connectorAddress(), req.connectorId());
    }
}

