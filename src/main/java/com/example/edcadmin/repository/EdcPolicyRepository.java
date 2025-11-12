package com.example.edcadmin.repository;

import com.example.edcadmin.model.policy.PolicyEnvelope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class EdcPolicyRepository {

    private final WebClient edcClient;
    private final ObjectMapper objectMapper;
    private final String managementPath;

    public EdcPolicyRepository(WebClient edcClient,
                              ObjectMapper objectMapper,
                              @Value("${app.edc.managementPath}") String managementPath) {
        this.edcClient = edcClient;
        this.objectMapper = objectMapper;
        this.managementPath = managementPath;
    }

    public Mono<String> create(String policyId) {
        var envelope = PolicyEnvelope.allowAll(policyId);
        var body = envelope.toJson();

        // Log JSON để debug
        try {
            String jsonString = objectMapper.writeValueAsString(body);
            System.out.println("Sending Policy JSON to EDC: " + jsonString);
        } catch (Exception e) {
            System.err.println("Error serializing Policy JSON: " + e.getMessage());
        }

        return edcClient.post()
                .uri(managementPath + "/policydefinitions")
                .header("X-Api-Key", "password")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Policy Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Policy API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }

    public Mono<String> findAll() {
        return edcClient.post()
                .uri(managementPath + "/policydefinitions/request")
                .header("X-Api-Key", "password")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Policies Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Policies API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }
}

