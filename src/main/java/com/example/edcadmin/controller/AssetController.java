package com.example.edcadmin.controller;

import com.example.edcadmin.model.AssetCreateRequest;
import com.example.edcadmin.model.AssetDetailResponse;
import com.example.edcadmin.service.EdcService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    private final EdcService edc;

    public AssetController(EdcService edc) { this.edc = edc; }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> create(@Valid @RequestBody AssetCreateRequest req) {
        return edc.createAsset(req);
    }

    @GetMapping(value = "/edc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> listOnEdc() {
        return edc.listAssetsEdc();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return edc.deleteAsset(id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AssetDetailResponse> detail(@PathVariable String id) {
        return edc.getAssetDetail(id);
    }
}
