package com.twd.SpringSecurityJWT.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.security.core.userdetails.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class FixedAssetCounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "asset_id", nullable = false)
    private FixedAsset asset;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date countDate;

    // @ManyToOne
    // @JoinColumn(name = "auditor_id", nullable = false)
    // private User auditor;

    @Column(nullable = false)
    private Integer quantityCounted;

   
    @Column(columnDefinition = "ENUM('YES', 'NO')", nullable = false)
    private ExistenceAsset existenceAsset = ExistenceAsset.YES;

    @Column(columnDefinition = "ENUM('normal', 'broken', 'lost')", nullable = false)
    private Condition condition = Condition.NORMAL;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "department_id")
    private Department department;

    @Lob
    private String remarks;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum ExistenceAsset {
        YES, NO
    }

    public enum Condition {
        NORMAL, BROKEN, LOST
    }
    
    // Getters and Setters
    // ...
}
