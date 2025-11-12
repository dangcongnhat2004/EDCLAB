package com.example.edcadmin.repository;

import com.example.edcadmin.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<PolicyEntity, String> { }
