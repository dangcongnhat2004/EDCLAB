package com.example.edcadmin.repository;

import com.example.edcadmin.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<AssetEntity, String> { }
