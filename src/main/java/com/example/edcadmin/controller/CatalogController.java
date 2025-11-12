package com.example.edcadmin.controller;

import com.example.edcadmin.model.Catalog;
import com.example.edcadmin.model.CatalogDetail;
import com.example.edcadmin.model.CatalogRequest;
import com.example.edcadmin.model.Solution;
import com.example.edcadmin.model.SolutionDetail;
import com.example.edcadmin.service.CatalogService;
import com.example.edcadmin.service.EdcService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class CatalogController {

    private final EdcService edc;
    private final CatalogService catalogService;

    public CatalogController(EdcService edc, CatalogService catalogService) {
        this.edc = edc;
        this.catalogService = catalogService;
    }

    // Existing endpoint - keep unchanged
    @PostMapping("/request")
    public Mono<String> request(@Valid @RequestBody CatalogRequest req) {
        return edc.fetchCatalog(req.providerUrl());
    }

    // New endpoints for catalog data
    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Catalog> getCatalogList() {
        return catalogService.getCatalogList();
    }

    @GetMapping(value = "/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CatalogDetail getCatalogDetail(@PathVariable String id) {
        return catalogService.getCatalogDetail(id);
    }

    @GetMapping(value = "/solution", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Solution> getSolutionList() {
        return catalogService.getSolutionList();
    }

    @GetMapping(value = "/solution/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolutionDetail getSolutionDetail(@PathVariable String id) {
        return catalogService.getSolutionDetail(id);
    }

    @GetMapping(value = "/data/{id}/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public CatalogDetail purchaseCatalog(@PathVariable String id) {
        return catalogService.getCatalogDetail(id);
    }
}
