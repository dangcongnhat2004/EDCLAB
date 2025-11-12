package com.example.edcadmin.controller;

import com.example.edcadmin.model.catalog.Catalog;
import com.example.edcadmin.model.catalog.CatalogDetail;
import com.example.edcadmin.model.solution.Solution;
import com.example.edcadmin.model.solution.SolutionDetail;
import com.example.edcadmin.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "*")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Catalog> getCatalogList() {
        return catalogService.getCatalogList();
    }

    @GetMapping(value = "/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CatalogDetail getCatalogDetail(@PathVariable String id) {
        return catalogService.getCatalogDetail(id);
    }

    @GetMapping(value = "/solution", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Solution> getSolutionList() {
        return catalogService.getSolutionList();
    }

    @GetMapping(value = "/solution/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SolutionDetail getSolutionDetail(@PathVariable String id) {
        return catalogService.getSolutionDetail(id);
    }

    @GetMapping(value = "/data/{id}/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public CatalogDetail purchaseCatalog(@PathVariable String id) {
        return catalogService.getCatalogDetail(id);
    }
}
