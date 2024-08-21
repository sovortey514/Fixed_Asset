package com.twd.SpringSecurityJWT.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "fixed_asset_details")
public class FixedAssetDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @ManyToOne
    @JoinColumn(name = "count_id", nullable = false)
    private FixedAssetCounts fixedAssetCount;

    @ManyToOne
    @JoinColumn(name = "asset_holder_id") // Foreign key reference to AssetHolder
    private AssetHolder assetHolder;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "asset_id")
    private FixedAsset fixedAsset;

    @Column(nullable = false)
    private Integer quantityCounted;

    @Column(name = "conditions", nullable = false, columnDefinition = "TEXT")
    private String conditions;

    @Column(name = "existenceAsset", nullable = false, columnDefinition = "TEXT")
    private String existenceAsset;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    public void setCreatedAt(LocalDateTime now) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCreatedAt'");
    }

    public void setUpdatedAt(LocalDateTime now) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUpdatedAt'");
    }

    public LocalDateTime getCreatedAt() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCreatedAt'");
    }

}