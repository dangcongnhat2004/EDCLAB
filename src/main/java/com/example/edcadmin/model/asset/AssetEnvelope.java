package com.example.edcadmin.model.asset;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class AssetEnvelope {
    private String id;
    private String name;
    private String description;
    private String contentType;
    private String baseUrl;
    private Map<String, String> customProperties;
    private AssetCreateRequest.DataAddressDetails dataAddressDetails;

    public Map<String, Object> toJson() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", name);
        properties.put("description", description);
        properties.put("contenttype", contentType);

        if (customProperties != null) {
            customProperties.forEach((key, value) -> {
                if (key != null && !key.isBlank() && value != null && !value.isBlank()) {
                    properties.put(key, value);
                }
            });
        }

        Map<String, Object> dataAddress = new HashMap<>();
        dataAddress.put("type", "HttpData");
        dataAddress.put("name", name);
        dataAddress.put("baseUrl", baseUrl);
        dataAddress.put("proxyPath", "true");

        if (dataAddressDetails != null) {
            if (dataAddressDetails.getEndpoint() != null && !dataAddressDetails.getEndpoint().isBlank()) {
                dataAddress.put("endpoint", dataAddressDetails.getEndpoint());
            }
            if (dataAddressDetails.getAuthenticationMethod() != null 
                    && !Objects.equals(dataAddressDetails.getAuthenticationMethod(), "none")
                    && !dataAddressDetails.getAuthenticationMethod().isBlank()) {
                dataAddress.put("authenticationMethod", dataAddressDetails.getAuthenticationMethod());
            }
        }

        Map<String, Object> context = new HashMap<>();
        context.put("@vocab", "https://w3id.org/edc/v0.0.1/ns/");

        Map<String, Object> result = new HashMap<>();
        result.put("@context", context);
        result.put("@id", id);
        result.put("properties", properties);
        result.put("dataAddress", dataAddress);

        return result;
    }

    public static AssetEnvelope of(AssetCreateRequest request) {
        AssetEnvelope envelope = new AssetEnvelope();
        envelope.setId(request.getId());
        envelope.setName(request.getName());
        envelope.setDescription(request.getDescription());
        envelope.setContentType(request.getContentType());
        envelope.setBaseUrl(request.getDataAddress());
        envelope.setCustomProperties(request.getProperties());
        envelope.setDataAddressDetails(request.getDataAddressDetails());
        return envelope;
    }
}
