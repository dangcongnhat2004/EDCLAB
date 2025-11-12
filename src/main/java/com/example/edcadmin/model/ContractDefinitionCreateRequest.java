package com.example.edcadmin.model;

import jakarta.validation.constraints.NotBlank;

public record ContractDefinitionCreateRequest(
        @NotBlank String id,
        @NotBlank String policyId
) {}

