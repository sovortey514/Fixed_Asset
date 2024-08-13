package com.twd.SpringSecurityJWT.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FixedAssetRequest {
    private String name;
    private String model;
    private Integer year;
    private Double price;
    private String serialNumber;
    private LocalDate purchaseDate;
    private String unit;
    private Integer quantity;
    private byte[] image;  // Consider if image is required or optional
    private Integer categoryId; 
    private Boolean assetExistence;  // Assuming category ID is included in the request
    private String status;
    public String getStatus() {
        return status;
    }
    // public String getStatustext() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getStatustext'");
    // }
    // public String getStatustext() {
    //     // TODO Auto-generated method stub
    //     return statustext;
    // }
  

    // Getters and Setters
}