package com.example.edcadmin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "policies")
public class PolicyEntity {
    @Id
    private String id;

    public PolicyEntity() { }
    public PolicyEntity(String id) { this.id = id; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
