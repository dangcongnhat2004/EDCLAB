package com.example.edcadmin.model.transfer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransferRequest {
    @NotBlank
    private String contractId;

    @NotBlank
    private String assetId;

    @NotBlank
    private String connectorAddress;

    @NotBlank
    private String connectorId;
}
