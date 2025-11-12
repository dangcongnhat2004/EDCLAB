package com.example.edcadmin.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class EdcContractNegotiationRepository {

    private final WebClient consumerClient;
    private final ObjectMapper objectMapper;
    private final String managementPath;

    public EdcContractNegotiationRepository(WebClient consumerClient,
                                           ObjectMapper objectMapper) {
        this.consumerClient = consumerClient;
        this.objectMapper = objectMapper;
        this.managementPath = "/v3";
    }

    public Mono<String> negotiate(String providerUrl, String contractOfferId, String assetId) {
        // Format đúng theo EDC v3 - khớp với Sample 00
        Map<String, Object> context = new HashMap<>();
        context.put("@vocab", "https://w3id.org/edc/v0.0.1/ns/");

        Map<String, Object> policy = new HashMap<>();
        policy.put("@context", "http://www.w3.org/ns/odrl.jsonld");
        policy.put("@id", contractOfferId);
        policy.put("@type", "Offer");
        policy.put("assigner", "provider");
        policy.put("target", assetId);

        Map<String, Object> body = new HashMap<>();
        body.put("@context", context);
        body.put("@type", "ContractRequest");
        body.put("counterPartyAddress", providerUrl);
        body.put("protocol", "dataspace-protocol-http");
        body.put("policy", policy);

        // Log JSON để debug
        try {
            String jsonString = objectMapper.writeValueAsString(body);
            System.out.println("Sending Contract Negotiation JSON to EDC: " + jsonString);
        } catch (Exception e) {
            System.err.println("Error serializing Contract Negotiation JSON: " + e.getMessage());
        }

        return consumerClient.post()
                .uri(managementPath + "/contractnegotiations")
                .header("X-Api-Key", "password")
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Contract Negotiation Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Contract Negotiation API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }
}

