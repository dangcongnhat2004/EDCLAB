package com.example.edcadmin.controller;

import com.example.edcadmin.model.catalog.CatalogRequest;
import com.example.edcadmin.service.ConsumerCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class EdcCatalogController {

    private final ConsumerCatalogService consumerCatalogService;

    public EdcCatalogController(ConsumerCatalogService consumerCatalogService) {
        this.consumerCatalogService = consumerCatalogService;
    }

    @PostMapping(value = "/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> request(@Valid @RequestBody CatalogRequest req) {
        return consumerCatalogService.fetchCatalog(req.getProviderUrl());
    }
}

