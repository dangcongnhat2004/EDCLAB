package com.example.edcadmin.model.asset;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class AssetCreateRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String contentType;

    @NotBlank
    private String dataAddress;

    private Map<String, String> properties;

    private DataAddressDetails dataAddressDetails;

    @Data
    public static class DataAddressDetails {
        private String endpoint;
        private String authenticationMethod;
    }
}
