package com.example.edcadmin.model.contract;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContractNegotiationRequest {
    @NotBlank
    private String providerUrl;

    @NotBlank
    private String contractOfferId;

    @NotBlank
    private String assetId;
}
