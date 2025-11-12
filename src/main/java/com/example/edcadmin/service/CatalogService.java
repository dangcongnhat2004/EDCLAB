package com.example.edcadmin.service;

import com.example.edcadmin.model.Catalog;
import com.example.edcadmin.model.CatalogDetail;
import com.example.edcadmin.model.Solution;
import com.example.edcadmin.model.SolutionDetail;
import com.example.edcadmin.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    public List<Catalog> getCatalogList() {
        try {
            return catalogRepository.getCatalogList();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public CatalogDetail getCatalogDetail(String id) {
        try {
            return catalogRepository.getCatalogDetail(id);
        } catch (IOException e) {
            e.printStackTrace();
            return new CatalogDetail();
        }
    }

    public List<Solution> getSolutionList() {
        try {
            return catalogRepository.getSolutionList();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public SolutionDetail getSolutionDetail(String id) {
        try {
            return catalogRepository.getSolutionDetail(id);
        } catch (IOException e) {
            e.printStackTrace();
            return new SolutionDetail();
        }
    }
}

