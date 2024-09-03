package com.twd.SpringSecurityJWT.dto;

import lombok.Data;

@Data
public class FileDataDTO {

    private String fileName;
    private String fileType;
    private byte[] fileContent;

    // Constructors, Getters, and Setters
}
