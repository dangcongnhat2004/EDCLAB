package com.example.edcadmin.controller;

import com.example.edcadmin.service.PolicyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/{id}")
    public Mono<String> create(@PathVariable String id) {
        return policyService.create(id);
    }

    @GetMapping(value = "/edc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> listOnEdc() {
        return policyService.findAll();
    }
}
