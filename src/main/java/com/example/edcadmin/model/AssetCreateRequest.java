package com.example.edcadmin.model;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record AssetCreateRequest(
        @NotBlank String id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String contentType,
        @NotBlank String dataAddress,
        Map<String, String> properties,
        DataAddressDetails dataAddressDetails
) {
    public record DataAddressDetails(
            String endpoint,
            String authenticationMethod
    ) {}
}
