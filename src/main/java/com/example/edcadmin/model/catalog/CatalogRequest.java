package com.example.edcadmin.model.catalog;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CatalogRequest {
    @NotBlank
    private String providerUrl;
}
