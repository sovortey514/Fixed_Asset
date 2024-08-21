package com.twd.SpringSecurityJWT.entity;

import java.time.LocalDateTime;
import java.util.Date;
// import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
// import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
// import jakarta.persistence.Temporal;
// import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class FixedAssetCounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countId;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;  // Assuming you have a Department entity

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date countDate;

    @OneToMany(mappedBy = "fixedAssetCount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FixedAssetDetail> assetDetails;
}