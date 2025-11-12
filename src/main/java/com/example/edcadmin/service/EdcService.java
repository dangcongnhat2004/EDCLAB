package com.example.edcadmin.service;

import com.example.edcadmin.model.AssetCreateRequest;
import com.example.edcadmin.model.AssetDetailResponse;
import com.example.edcadmin.repository.EdcAssetRepository;
import com.example.edcadmin.repository.EdcContractDefinitionRepository;
import com.example.edcadmin.repository.EdcPolicyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class EdcService {

    private final EdcAssetRepository assetRepository;
    private final EdcPolicyRepository policyRepository;
    private final EdcContractDefinitionRepository contractDefinitionRepository;
    private final WebClient consumerClient;
    private final ObjectMapper objectMapper;

    @Value("${app.edc.managementPath}")
    private String managementPath;

    public EdcService(EdcAssetRepository assetRepository,
                      EdcPolicyRepository policyRepository,
                      EdcContractDefinitionRepository contractDefinitionRepository,
                      WebClient consumerClient,
                      ObjectMapper objectMapper) {
        this.assetRepository = assetRepository;
        this.policyRepository = policyRepository;
        this.contractDefinitionRepository = contractDefinitionRepository;
        this.consumerClient = consumerClient;
        this.objectMapper = objectMapper;
    }

    // -------- Assets ----------
    public Mono<String> createAsset(AssetCreateRequest req) {
        return assetRepository.create(req);
    }

    public Mono<String> listAssetsEdc() {
        return assetRepository.findAll();
    }

    public Mono<Void> deleteAsset(String assetId) {
        return assetRepository.deleteById(assetId);
    }

    public Mono<AssetDetailResponse> getAssetDetail(String assetId) {
        return assetRepository.findById(assetId);
    }

    // -------- Policies ----------
    public Mono<String> createPolicy(String policyId) {
        return policyRepository.create(policyId);
    }

    public Mono<String> listPoliciesEdc() {
        return policyRepository.findAll();
    }

    // -------- Contract Definitions ----------
    public Mono<String> createContractDef(String id, String policyId) {
        return contractDefinitionRepository.create(id, policyId);
    }

    public Mono<String> listContractDefsEdc() {
        return contractDefinitionRepository.findAll();
    }

    // -------- Catalog (Consumer) ----------
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

    // -------- Contract Negotiation (Consumer) ----------
    public Mono<String> negotiateContract(String providerUrl, String contractOfferId, String assetId) {
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

    // -------- Transfer Data (Consumer) ----------
    public Mono<String> transferData(String contractId, String assetId, String connectorAddress, String connectorId) {
        Map<String, Object> body = new java.util.HashMap<>();
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
