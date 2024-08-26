package com.twd.SpringSecurityJWT.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.twd.SpringSecurityJWT.entity.AssetHolder;
import com.twd.SpringSecurityJWT.entity.Building;
import com.twd.SpringSecurityJWT.entity.Category;
import com.twd.SpringSecurityJWT.entity.Department;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.entity.FixedAssetCounts;
import com.twd.SpringSecurityJWT.entity.FixedAssetDetail;
import com.twd.SpringSecurityJWT.entity.OurUsers;
import com.twd.SpringSecurityJWT.entity.Product;
import com.twd.SpringSecurityJWT.entity.Room;

import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String role;
    private String password;
    private String phoneNumber;
    private List<Product> products;
    private Category category;
    private List<Category> categories;
    private OurUsers ourUsers;
    private FixedAsset fixedAsset;
    private List<FixedAsset> fixedAssets;
    private Building building;
    private List<Building> buildings; 
   
    private Department department;
    private List<Department> departments;

    private Room room;
    private List<Room> rooms;

    private AssetHolder assetHolder;
    private List<AssetHolder> assetHolders;

    private FixedAssetDetail fixedAssetDetail;
    private List<FixedAssetDetail> fixedAssetDetails;

    private FixedAssetCounts fixedAssetCounts;
    private List<FixedAssetCounts> fixedAssetCounts2;

    private String status;
    private String statustext;

}