package com.example.edcadmin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CatalogDetail {
    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type;

    @JsonProperty("dcat:dataset")
    private CatalogDataset catalogDataset;

    private List<AssetInfo> assets;

    @Data
    public static class CatalogDataset {
        private String title;
        private String description;
        private String provider;

        @JsonProperty("img_url")
        private String imgUrl;
    }

    @Data
    public static class AssetInfo {
        @JsonProperty("@id")
        private String id;

        @JsonProperty("@type")
        private String type;

        private Properties properties;

        @Data
        public static class Properties {
            private String name;
            private String id;
            private String contenttype;
        }
    }
}

