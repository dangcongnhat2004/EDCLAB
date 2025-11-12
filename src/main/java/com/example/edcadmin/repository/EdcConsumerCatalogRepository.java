package com.example.edcadmin.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class EdcConsumerCatalogRepository {

    private final WebClient consumerClient;
    private final ObjectMapper objectMapper;
    private final String managementPath;

    public EdcConsumerCatalogRepository(WebClient consumerClient,
                                       ObjectMapper objectMapper) {
        this.consumerClient = consumerClient;
        this.objectMapper = objectMapper;
        this.managementPath = "/v3";
    }

    public Mono<String> fetchCatalog(String providerProtocolUrl) {
        // Format đúng theo EDC v3 - khớp với Sample 00
        Map<String, Object> context = new HashMap<>();
        context.put("@vocab", "https://w3id.org/edc/v0.0.1/ns/");

        Map<String, Object> body = new HashMap<>();
        body.put("@context", context);
        body.put("counterPartyAddress", providerProtocolUrl);
        body.put("protocol", "dataspace-protocol-http");

        // Log JSON để debug
        try {
            String jsonString = objectMapper.writeValueAsString(body);
            System.out.println("Sending Catalog Request JSON to EDC: " + jsonString);
        } catch (Exception e) {
            System.err.println("Error serializing Catalog Request JSON: " + e.getMessage());
        }

        return consumerClient.post()
                .uri(managementPath + "/catalog/request")
                .header("X-Api-Key", "password")
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Catalog Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Catalog API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }
}

