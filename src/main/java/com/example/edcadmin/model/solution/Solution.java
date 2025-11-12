package com.example.edcadmin.model.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Solution {
    @JsonProperty("@id")
    private String id;

    private String title;

    private String description;

    private String provider;

    @JsonProperty("img_url")
    private String imgUrl;

    private boolean purchased;
}

