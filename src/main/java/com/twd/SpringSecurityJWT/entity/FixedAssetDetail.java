package com.twd.SpringSecurityJWT.entity;



import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "fixed_asset_details")
public class FixedAssetDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_holder_id") 
    private AssetHolder assetHolder;

    @ManyToOne(fetch = FetchType.EAGER)
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

    
    @ManyToOne
    @JoinColumn(name = "fixedassetdetail", nullable = false)
    // @JsonIgnore
    private FixedAssetCounts fixedAssetCount;

}