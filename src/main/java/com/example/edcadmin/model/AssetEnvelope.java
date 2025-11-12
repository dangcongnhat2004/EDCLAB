package com.example.edcadmin.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record AssetEnvelope(
        String id,
        String name,
        String description,
        String contentType,
        String baseUrl,
        Map<String, String> customProperties,
        AssetCreateRequest.DataAddressDetails dataAddressDetails
) {

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
            if (dataAddressDetails.endpoint() != null && !dataAddressDetails.endpoint().isBlank()) {
                dataAddress.put("endpoint", dataAddressDetails.endpoint());
            }
            if (dataAddressDetails.authenticationMethod() != null && !Objects.equals(dataAddressDetails.authenticationMethod(), "none")
                    && !dataAddressDetails.authenticationMethod().isBlank()) {
                dataAddress.put("authenticationMethod", dataAddressDetails.authenticationMethod());
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
        return new AssetEnvelope(
                request.id(),
                request.name(),
                request.description(),
                request.contentType(),
                request.dataAddress(),
                request.properties(),
                request.dataAddressDetails()
        );
    }
}
