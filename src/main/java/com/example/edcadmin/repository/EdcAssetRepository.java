package com.example.edcadmin.repository;

import com.example.edcadmin.model.asset.AssetCreateRequest;
import com.example.edcadmin.model.asset.AssetDetailResponse;
import com.example.edcadmin.model.asset.AssetEnvelope;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EdcAssetRepository {

    private final WebClient edcClient;
    private final ObjectMapper objectMapper;
    private final String managementPath;

    public EdcAssetRepository(WebClient edcClient,
                              ObjectMapper objectMapper,
                              @Value("${app.edc.managementPath}") String managementPath) {
        this.edcClient = edcClient;
        this.objectMapper = objectMapper;
        this.managementPath = managementPath;
    }

    public Mono<String> create(AssetCreateRequest req) {
        var json = AssetEnvelope.of(req).toJson();

        // Log JSON để debug
        try {
            String jsonString = objectMapper.writeValueAsString(json);
            System.out.println("Sending JSON to EDC: " + jsonString);
        } catch (Exception e) {
            System.err.println("Error serializing JSON: " + e.getMessage());
        }

        return edcClient.post()
                .uri(managementPath + "/assets")
                .header("X-Api-Key", "password")
                .header("Content-Type", "application/json")
                .bodyValue(json)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }

    public Mono<String> findAll() {
        return edcClient.post()
                .uri(managementPath + "/assets/request")
                .header("X-Api-Key", "password")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Assets Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Assets API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }

    public Mono<Void> deleteById(String assetId) {
        return edcClient.delete()
                .uri(managementPath + "/assets/{id}", assetId)
                .header("X-Api-Key", "password")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Delete Asset Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Delete Asset API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(Void.class);
    }

    public Mono<AssetDetailResponse> findById(String assetId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("operandLeft", "@id");
        filter.put("operator", "=");
        filter.put("operandRight", assetId);

        Map<String, Object> querySpec = new HashMap<>();
        querySpec.put("@type", "QuerySpec");
        querySpec.put("limit", 1);
        querySpec.put("filterExpression", List.of(filter));

        return edcClient.post()
                .uri(managementPath + "/assets/request")
                .header("X-Api-Key", "password")
                .header("Content-Type", "application/json")
                .bodyValue(querySpec)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Asset Detail Error Response: " + errorBody);
                                    HttpStatusCode statusCode = response.statusCode();
                                    String message = statusCode == HttpStatus.NOT_FOUND
                                            ? "Asset not found"
                                            : "EDC Asset Detail API Error: " + statusCode;
                                    return Mono.error(new ResponseStatusException(statusCode, message));
                                }))
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .map(results -> {
                    if (results == null || results.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found");
                    }
                    return AssetDetailResponse.fromEdc(results.get(0));
                });
    }
}

