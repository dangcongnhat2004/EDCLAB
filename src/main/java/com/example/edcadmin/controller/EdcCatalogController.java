package com.example.edcadmin.controller;

import com.example.edcadmin.model.CatalogRequest;
import com.example.edcadmin.service.EdcService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class EdcCatalogController {

    private final EdcService edc;

    public EdcCatalogController(EdcService edc) {
        this.edc = edc;
    }

    @PostMapping(value = "/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> request(@Valid @RequestBody CatalogRequest req) {
        return edc.fetchCatalog(req.providerUrl());
    }
}

