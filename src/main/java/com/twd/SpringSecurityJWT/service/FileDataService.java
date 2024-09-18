package com.twd.SpringSecurityJWT.service;

import org.springframework.web.multipart.MultipartFile;

import com.twd.SpringSecurityJWT.dto.FixedAssetFileResponseDTO;

import java.io.IOException;
import java.util.List;
public interface FileDataService {

    String uploadFileToFileDirectory(MultipartFile file, Long fixedAssetId) throws IOException;

    String uploadFileToUserDirectory(MultipartFile file, Long userId) throws IOException;

    byte[] downloadFileFromFileDirectory(String fileName) throws IOException;

    FixedAssetFileResponseDTO downloadAllFilesByFixedAssetId(Long fixedAssetId) throws IOException;

    List<FixedAssetFileResponseDTO> getAllAssetsWithImages() throws IOException;
}
