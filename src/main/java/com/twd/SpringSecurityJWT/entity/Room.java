package com.twd.SpringSecurityJWT.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

import javax.naming.Binding;
@Data
@Entity
public class Room {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;


    @Column(nullable = false)
    private String name;

    private String description;

    private Integer capacity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

   

    



    
}
