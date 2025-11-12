package com.example.edcadmin.service;

import com.example.edcadmin.repository.EdcTransferRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransferService {

    private final EdcTransferRepository transferRepository;

    public TransferService(EdcTransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Mono<String> transfer(String contractId, String assetId, String connectorAddress, String connectorId) {
        return transferRepository.transfer(contractId, assetId, connectorAddress, connectorId);
    }
}

