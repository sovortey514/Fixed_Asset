package com.twd.SpringSecurityJWT.entity;

import java.time.LocalDateTime;

import org.hibernate.mapping.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;
    
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // public String getName() {
    //     return name;
    // }


    
}