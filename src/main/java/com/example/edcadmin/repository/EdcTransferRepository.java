package com.example.edcadmin.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class EdcTransferRepository {

    private final WebClient consumerClient;
    private final ObjectMapper objectMapper;
    private final String managementPath;

    public EdcTransferRepository(WebClient consumerClient,
                                 ObjectMapper objectMapper,
                                 @Value("${app.edc.managementPath}") String managementPath) {
        this.consumerClient = consumerClient;
        this.objectMapper = objectMapper;
        this.managementPath = managementPath;
    }

    public Mono<String> transfer(String contractId, String assetId, String connectorAddress, String connectorId) {
        Map<String, Object> body = new HashMap<>();
        body.put("connectorAddress", connectorAddress);
        body.put("connectorId", connectorId);
        body.put("contractId", contractId);
        body.put("assetId", assetId);
        body.put("protocol", "dataspace-protocol-http");
        body.put("dataDestination", Map.of("type", "HttpData"));

        // Log JSON để debug
        try {
            String jsonString = objectMapper.writeValueAsString(body);
            System.out.println("Sending Transfer Request JSON to EDC: " + jsonString);
        } catch (Exception e) {
            System.err.println("Error serializing Transfer Request JSON: " + e.getMessage());
        }

        return consumerClient.post()
                .uri(managementPath + "/transferprocesses")
                .header("X-Api-Key", "password")
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Transfer Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Transfer API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }
}

