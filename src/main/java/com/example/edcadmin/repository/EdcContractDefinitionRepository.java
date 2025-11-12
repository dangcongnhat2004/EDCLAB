package com.example.edcadmin.repository;

import com.example.edcadmin.model.contract.ContractDefinitionEnvelope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class EdcContractDefinitionRepository {

    private final WebClient edcClient;
    private final ObjectMapper objectMapper;
    private final String managementPath;

    public EdcContractDefinitionRepository(WebClient edcClient,
                                         ObjectMapper objectMapper,
                                         @Value("${app.edc.managementPath}") String managementPath) {
        this.edcClient = edcClient;
        this.objectMapper = objectMapper;
        this.managementPath = managementPath;
    }

    public Mono<String> create(String id, String policyId) {
        var envelope = ContractDefinitionEnvelope.all(id, policyId);
        var body = envelope.toJson();

        // Log JSON để debug
        try {
            String jsonString = objectMapper.writeValueAsString(body);
            System.out.println("Sending Contract Definition JSON to EDC: " + jsonString);
        } catch (Exception e) {
            System.err.println("Error serializing Contract Definition JSON: " + e.getMessage());
        }

        return edcClient.post()
                .uri(managementPath + "/contractdefinitions")
                .header("X-Api-Key", "password")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Contract Definition Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Contract Definition API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }

    public Mono<String> findAll() {
        return edcClient.post()
                .uri(managementPath + "/contractdefinitions/request")
                .header("X-Api-Key", "password")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Contract Definitions Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Contract Definitions API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }
}

