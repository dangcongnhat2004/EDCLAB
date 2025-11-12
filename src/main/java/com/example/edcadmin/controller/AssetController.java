package com.example.edcadmin.controller;

import com.example.edcadmin.model.asset.AssetCreateRequest;
import com.example.edcadmin.model.asset.AssetDetailResponse;
import com.example.edcadmin.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> create(@Valid @RequestBody AssetCreateRequest req) {
        return assetService.create(req);
    }

    @GetMapping(value = "/edc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> listOnEdc() {
        return assetService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return assetService.deleteById(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AssetDetailResponse> detail(@PathVariable String id) {
        return assetService.findById(id);
    }
}
