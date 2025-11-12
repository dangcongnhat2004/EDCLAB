package com.example.edcadmin.service;

import com.example.edcadmin.model.asset.AssetCreateRequest;
import com.example.edcadmin.model.asset.AssetDetailResponse;
import com.example.edcadmin.repository.EdcAssetRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AssetService {

    private final EdcAssetRepository assetRepository;

    public AssetService(EdcAssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Mono<String> create(AssetCreateRequest req) {
        return assetRepository.create(req);
    }

    public Mono<String> findAll() {
        return assetRepository.findAll();
    }

    public Mono<Void> deleteById(String assetId) {
        return assetRepository.deleteById(assetId);
    }

    public Mono<AssetDetailResponse> findById(String assetId) {
        return assetRepository.findById(assetId);
    }
}

