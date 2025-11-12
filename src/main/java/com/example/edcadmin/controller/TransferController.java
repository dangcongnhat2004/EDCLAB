package com.example.edcadmin.controller;

import com.example.edcadmin.model.transfer.TransferRequest;
import com.example.edcadmin.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> transfer(@Valid @RequestBody TransferRequest req) {
        return transferService.transfer(req.getContractId(), req.getAssetId(), req.getConnectorAddress(), req.getConnectorId());
    }
}

