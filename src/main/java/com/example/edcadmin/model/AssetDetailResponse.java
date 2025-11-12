package com.example.edcadmin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record AssetDetailResponse(
        String id,
        String name,
        String description,
        String contentType,
        String dataAddress,
        String endpoint,
        String authenticationMethod,
        List<PropertyItem> properties
) {
    private static final Set<String> CORE_PROPERTY_KEYS = Set.of(
            "id", "@id", "name", "description", "contenttype"
    );

    public record PropertyItem(String key, String value) {}

    public static AssetDetailResponse fromEdc(Map<String, Object> asset) {
        if (asset == null || asset.isEmpty()) {
            return new AssetDetailResponse("", "", "", "", "", null, null, Collections.emptyList());
        }

        String id = asString(asset.get("@id"));

        Map<String, Object> properties = extractMap(asset.get("properties"));
        Map<String, Object> dataAddress = extractMap(asset.get("dataAddress"));

        String name = asString(properties.get("name"));
        String description = asString(properties.get("description"));
        String contentType = asString(properties.get("contenttype"));
        String baseUrl = asString(dataAddress.get("baseUrl"));
        String endpoint = asString(dataAddress.get("endpoint"));
        String authenticationMethod = formatAuthMethod(asString(dataAddress.get("authenticationMethod")));

        List<PropertyItem> propertyItems = new ArrayList<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (!CORE_PROPERTY_KEYS.contains(entry.getKey())) {
                propertyItems.add(new PropertyItem(entry.getKey(), asString(entry.getValue())));
            }
        }

        return new AssetDetailResponse(
                id,
                name,
                description,
                contentType,
                baseUrl,
                endpoint,
                authenticationMethod,
                propertyItems
        );
    }

    private static Map<String, Object> extractMap(Object source) {
        if (source instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            map.forEach((k, v) -> result.put(String.valueOf(k), v));
            return result;
        }
        return Collections.emptyMap();
    }

    private static String asString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static String formatAuthMethod(String method) {
        if (method == null || method.isBlank()) {
            return null;
        }
        String normalized = method.trim().toLowerCase();
        return switch (normalized) {
            case "apikey", "api_key", "api-key" -> "API Key";
            case "basic" -> "Basic";
            case "oauth2", "oauth" -> "OAuth2";
            case "bearer" -> "Bearer Token";
            case "none" -> null;
            default -> method;
        };
    }
}
