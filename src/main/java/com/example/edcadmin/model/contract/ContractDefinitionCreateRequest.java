package com.example.edcadmin.model.contract;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContractDefinitionCreateRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String policyId;
}
