package com.example.edcadmin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Solution {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("dcat:dataset")
    private SolutionDataset solutionDataset;

    @Data
    public static class SolutionDataset {
        private String title;
        private String description;
        private String provider;

        @JsonProperty("img_url")
        private String imgUrl;
    }
}

