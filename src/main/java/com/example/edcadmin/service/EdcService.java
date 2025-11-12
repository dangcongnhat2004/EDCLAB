package com.example.edcadmin.service;

import com.example.edcadmin.entity.AssetEntity;
import com.example.edcadmin.entity.ContractDefinitionEntity;
import com.example.edcadmin.entity.PolicyEntity;
import com.example.edcadmin.model.AssetCreateRequest;
import com.example.edcadmin.model.AssetEnvelope;
import com.example.edcadmin.model.ContractDefinitionEnvelope;
import com.example.edcadmin.model.PolicyEnvelope;
import com.example.edcadmin.repository.AssetRepository;
import com.example.edcadmin.repository.ContractDefinitionRepository;
import com.example.edcadmin.repository.PolicyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class EdcService {

    private final WebClient edcClient;
    private final WebClient consumerClient;

    private final AssetRepository assetRepo;
    private final PolicyRepository policyRepo;
    private final ContractDefinitionRepository contractRepo;
    private final ObjectMapper objectMapper;

    @Value("${app.edc.managementPath}")
    private String managementPath;

    public EdcService(WebClient edcClient,
                      WebClient consumerClient,
                      AssetRepository assetRepo,
                      PolicyRepository policyRepo,
                      ContractDefinitionRepository contractRepo,
                      ObjectMapper objectMapper) {
        this.edcClient = edcClient;
        this.consumerClient = consumerClient;
        this.assetRepo = assetRepo;
        this.policyRepo = policyRepo;
        this.contractRepo = contractRepo;
        this.objectMapper = objectMapper;
    }

    // -------- Assets ----------
    public Mono<String> createAsset(AssetCreateRequest req) {
        // lưu local DB
        AssetEntity entity = new AssetEntity();
        entity.setId(req.id());
        entity.setName(req.name());
        entity.setDescription(req.description());
        entity.setContentType(req.contentType());
        entity.setBaseUrl(req.dataAddress());
        if (req.dataAddressDetails() != null) {
            var details = req.dataAddressDetails();
            if (details.endpoint() != null && !details.endpoint().isBlank()) {
                entity.setEndpoint(details.endpoint());
            }
            if (details.authenticationMethod() != null
                    && !details.authenticationMethod().isBlank()
                    && !"none".equalsIgnoreCase(details.authenticationMethod())) {
                entity.setAuthenticationMethod(details.authenticationMethod());
            }
        }
        if (req.properties() != null && !req.properties().isEmpty()) {
            try {
                entity.setPropertiesJson(objectMapper.writeValueAsString(req.properties()));
            } catch (Exception e) {
                System.err.println("Error serializing asset properties: " + e.getMessage());
            }
        }
        assetRepo.save(entity);

        // publish lên EDC
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
                .header("X-Api-Key", "password") // QUAN TRỌNG
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

    public Mono<String> listAssetsEdc() {
        // EDC Management API v3 - POST to /assets/request without body to list all assets
        // In Postman, sending POST without any body works (200 OK)
        // Try without Content-Type header and without body
        return edcClient.post()
                .uri(managementPath + "/assets/request")
                .header("X-Api-Key", "password")
                // Don't set Content-Type when there's no body
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Assets Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Assets API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }

    public Mono<Void> deleteAsset(String assetId) {
        assetRepo.deleteById(assetId);
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

    // -------- Policies ----------
    public Mono<String> createPolicy(String policyId) {
        policyRepo.save(new PolicyEntity(policyId));
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

    public Mono<String> listPoliciesEdc() {
        // EDC Management API v3 - POST to /policydefinitions/request without body to list all policies
        return edcClient.post()
                .uri(managementPath + "/policydefinitions/request")
                .header("X-Api-Key", "password")
                // Don't set Content-Type when there's no body
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Policies Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Policies API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
    }

    // -------- Contract Definitions ----------
    public Mono<String> createContractDef(String id, String policyId) {
        var entity = new ContractDefinitionEntity();
        entity.setId(id);
        entity.setPolicyId(policyId);
        contractRepo.save(entity);

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

    public Mono<String> listContractDefsEdc() {
        // EDC Management API v3 - POST to /contractdefinitions/request without body to list all contract definitions
        return edcClient.post()
                .uri(managementPath + "/contractdefinitions/request")
                .header("X-Api-Key", "password")
                // Don't set Content-Type when there's no body
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("EDC Contract Definitions Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("EDC Contract Definitions API Error: " + response.statusCode() + " - " + errorBody));
                                }))
                .bodyToMono(String.class);
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
