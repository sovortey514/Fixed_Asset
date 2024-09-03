package com.twd.SpringSecurityJWT.dto;

import java.time.LocalDate;
import java.util.List;

import com.twd.SpringSecurityJWT.entity.AssetHolder;

import lombok.Data;

@Data
public class FixedAssetFileResponseDTO {

    private Long fixedAssetId;
    private String fixedAssetName;
    private String fixedAssetCategory;
    private String fixedAssetModel;
    private Integer fixedAssetYear;
    private Double fixedAssetPrice;
    private String fixedAssetSerialNumber;
    private LocalDate fixedAssetPurchaseDate;
    private String fixedAssetUnit;
    private Integer fixedAssetQuantity;
    private String fixedAssetRemarks;
    private String fixedAssetStatus;
    private String fixedAssetStatusText;
    private String fixedAssetUser;
    private String fixedAssetBuilding;
    private String fixedAssetAssetHolder;

    private List<FileDataDTO> files;

}
