package com.example.edcadmin.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ContractDefinitionEnvelope(
        String id,
        String accessPolicyId,
        String contractPolicyId,
        List<Object> assetsSelector
) {
    
    public Map<String, Object> toJson() {
        // Format đúng theo EDC v3
        Map<String, Object> context = new HashMap<>();
        context.put("@vocab", "https://w3id.org/edc/v0.0.1/ns/");
        
        Map<String, Object> result = new HashMap<>();
        result.put("@context", context);
        result.put("@id", id);
        result.put("accessPolicyId", accessPolicyId);
        result.put("contractPolicyId", contractPolicyId);
        result.put("assetsSelector", assetsSelector);
        
        return result;
    }
    
    public static ContractDefinitionEnvelope all(String id, String policyId) {
        return new ContractDefinitionEnvelope(
                id,
                policyId,
                policyId,
                List.of() // áp dụng cho tất cả asset (empty array)
        );
    }
}
