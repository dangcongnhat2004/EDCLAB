package com.example.edcadmin.model.policy;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PolicyEnvelope {
    private String id;
    private Map<String, Object> policy;
    
    public Map<String, Object> toJson() {
        // Format đúng theo EDC v3 - tương tự AssetEnvelope và ContractDefinitionEnvelope
        Map<String, Object> context = new HashMap<>();
        context.put("@vocab", "https://w3id.org/edc/v0.0.1/ns/");
        context.put("odrl", "http://www.w3.org/ns/odrl/2/");
        
        Map<String, Object> result = new HashMap<>();
        result.put("@context", context);
        result.put("@id", id);
        result.put("policy", policy); // Key "policy" bọc ngoài policy object
        
        return result;
    }
    
    public static PolicyEnvelope allowAll(String id) {
        // Format policy theo chuẩn ODRL cho EDC v3 - khớp với Sample 00
        Map<String, Object> policy = new HashMap<>();
        policy.put("@context", "http://www.w3.org/ns/odrl.jsonld");
        policy.put("@type", "Set");
        policy.put("permission", List.of()); // Array rỗng = allow all
        policy.put("prohibition", List.of());
        policy.put("obligation", List.of());
        
        PolicyEnvelope envelope = new PolicyEnvelope();
        envelope.setId(id);
        envelope.setPolicy(policy);
        return envelope;
    }
}
