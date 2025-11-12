package com.example.edcadmin.repository;

import com.example.edcadmin.model.catalog.Catalog;
import com.example.edcadmin.model.catalog.CatalogDetail;
import com.example.edcadmin.model.solution.Solution;
import com.example.edcadmin.model.solution.SolutionDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
public class CatalogRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Catalog> getCatalogList() throws IOException {
        String fileName = "catalog_list.json";
        try (InputStream is = getClass().getResourceAsStream("/data/" + fileName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return objectMapper.readValue(is, new TypeReference<List<Catalog>>() {});
        }
    }

    public CatalogDetail getCatalogDetail(String id) throws IOException {
        String fileName = "catalog_detail.json";
        try (InputStream is = getClass().getResourceAsStream("/data/" + fileName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            // Read all details and filter by id
            List<CatalogDetail> details = objectMapper.readValue(is, new TypeReference<List<CatalogDetail>>() {});
            return details.stream()
                    .filter(detail -> detail.getId() != null && detail.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IOException("Catalog detail not found for id: " + id));
        }
    }

    public List<Solution> getSolutionList() throws IOException {
        String fileName = "solution_list.json";
        try (InputStream is = getClass().getResourceAsStream("/data/" + fileName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return objectMapper.readValue(is, new TypeReference<List<Solution>>() {});
        }
    }

    public SolutionDetail getSolutionDetail(String id) throws IOException {
        String fileName = "solution_detail.json";
        try (InputStream is = getClass().getResourceAsStream("/data/" + fileName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            // Read all details and filter by id
            List<SolutionDetail> details = objectMapper.readValue(is, new TypeReference<List<SolutionDetail>>() {});
            return details.stream()
                    .filter(detail -> detail.getId() != null && detail.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IOException("Solution detail not found for id: " + id));
        }
    }
}

