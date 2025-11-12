package com.example.edcadmin.model;

import jakarta.validation.constraints.NotBlank;

public record TransferRequest(
        @NotBlank String contractId,
        @NotBlank String assetId,
        @NotBlank String connectorAddress,
        @NotBlank String connectorId
) {}

