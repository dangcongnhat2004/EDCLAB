package com.example.edcadmin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SolutionDetail {
    @JsonProperty("@id")
    private String id;

    private String title;

    private String description;

    private String provider;

    @JsonProperty("data_type")
    private String data_type;

    private String format;

    @JsonProperty("last_updated")
    private String lastUpdated;

    @JsonProperty("contract_terms")
    private String contractTerms;
}

