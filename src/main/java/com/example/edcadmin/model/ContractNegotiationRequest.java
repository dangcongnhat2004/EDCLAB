package com.example.edcadmin.model;

import jakarta.validation.constraints.NotBlank;

public record ContractNegotiationRequest(
        @NotBlank String providerUrl,
        @NotBlank String contractOfferId,
        @NotBlank String assetId
) {}

