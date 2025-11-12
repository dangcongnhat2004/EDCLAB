package com.example.edcadmin.model;

import jakarta.validation.constraints.NotBlank;

public record CatalogRequest(
        @NotBlank String providerUrl
) {}

