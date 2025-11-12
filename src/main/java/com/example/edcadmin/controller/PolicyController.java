package com.example.edcadmin.controller;

import com.example.edcadmin.service.EdcService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final EdcService edc;

    public PolicyController(EdcService edc) { this.edc = edc; }

    @PostMapping("/{id}")
    public Mono<String> create(@PathVariable String id) {
        return edc.createPolicy(id);
    }

    @GetMapping(value = "/edc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> listOnEdc() {
        return edc.listPoliciesEdc();
    }
}
