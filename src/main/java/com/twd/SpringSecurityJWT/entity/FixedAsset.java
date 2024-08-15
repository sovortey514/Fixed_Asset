package com.twd.SpringSecurityJWT.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "fixed_assets")
public class FixedAsset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String model;
    
    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Double price; 

    @Column(length = 100, unique = true)
    private String serialNumber;

    private LocalDate purchaseDate;

    @Column(length = 50)
    private String unit;

    private Integer quantity;

    // @Column(nullable = false)
    // private Boolean assetExistence;


    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(nullable = true)
    private String status= "1";

    @Column(nullable = false)
    private String statustext;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private OurUsers user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "asset_holder_id") // Foreign key column
    private AssetHolder assetHolder; // Relationship to AssetHolder

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}