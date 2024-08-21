package com.twd.SpringSecurityJWT.entity;

import java.time.LocalDateTime;



import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "AssetHolder")
public class AssetHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 255, nullable = false, unique = true)
    private String email;


    @Column(length = 20, nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false, foreignKey = @ForeignKey(name = "FKad19cdfm14yb12d83y6skdtx3", foreignKeyDefinition = "FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE CASCADE"))
    private Department department;

    private LocalDateTime givenDate;
    private LocalDateTime returnDate;

    @Column(length = 255)
    private String returnNote;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @OneToMany(mappedBy = "assetHolder", cascade = CascadeType.REMOVE) 
    @JsonBackReference// Reference to the field in FixedAsset
    private List<FixedAsset> fixedAssets;
   
    private LocalDateTime assignedAt;
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
}



