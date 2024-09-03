package com.twd.SpringSecurityJWT.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.twd.SpringSecurityJWT.dto.FileDataDTO;
import com.twd.SpringSecurityJWT.dto.FixedAssetFileResponseDTO;
import com.twd.SpringSecurityJWT.entity.FileData;
import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.repository.FileDataRepository;
import com.twd.SpringSecurityJWT.repository.FixedAssetRepository;
import com.twd.SpringSecurityJWT.service.FileDataService;

@Service
public class FileDataServiceImpl implements FileDataService{
	
	@Autowired
	private FileDataRepository fileDataRepository;

    @Autowired
    private FixedAssetRepository fixedAssetRepository;
	
	
	private final String FILE_PATH = "D:\\Year4\\project_Intern\\Fixed_Asset\\src\\Uploads\\";

    @Override
public String uploadFileToFileDirectory(MultipartFile file, Long fixedAssetId) throws IOException {
    // Check if fixedAssetId is provided
    if (fixedAssetId == null) {
        throw new IllegalArgumentException("FixedAsset ID must not be null");
    }

    // Retrieve FixedAsset from the database
    Optional<FixedAsset> fixedAssetOpt = fixedAssetRepository.findById(fixedAssetId);
    if (!fixedAssetOpt.isPresent()) {
        throw new IOException("FixedAsset not found with ID: " + fixedAssetId);
    }

    FixedAsset fixedAsset = fixedAssetOpt.get();
    String filePath = FILE_PATH + file.getOriginalFilename(); // Absolute path

    // Create and save FileData with the fixed asset association
    FileData fileData = FileData.builder()
            .name(file.getOriginalFilename())
            .type(file.getContentType())
            .filePath(filePath)
            .fixedAsset(fixedAsset) // Associate the file with the fixed asset
            .build();

    // Save file data to the database
    FileData savedFileData = fileDataRepository.save(fileData);
    
    // Save the file to the file system
    file.transferTo(new java.io.File(filePath)); 
    
    if (savedFileData != null) {
        return "File uploaded successfully: " + file.getOriginalFilename() + " and Files uploaded path is: " + filePath;
    } else {
        throw new IOException("Failed to save file data to the database");
    }
}

    @Override
    public byte[] downloadFileFromFileDirectory(String fileName) throws IOException {
        Optional<FileData> fileDataObj = fileDataRepository.findByName(fileName);
        
        if (fileDataObj.isPresent()) {
            String filePath = fileDataObj.get().getFilePath();
            Path path = Paths.get(filePath);
            
            // Check if file exists before reading
            if (Files.exists(path)) {
                return Files.readAllBytes(path);
            } else {
                throw new IOException("File not found");
            }
        } else {
            throw new IOException("File data not found in the database");
        }
    }

     @Override
    public FixedAssetFileResponseDTO downloadAllFilesByFixedAssetId(Long fixedAssetId) throws IOException {
        // Check if fixedAssetId is provided
        if (fixedAssetId == null) {
            throw new IllegalArgumentException("FixedAsset ID must not be null");
        }

        // Retrieve FixedAsset from the database
        Optional<FixedAsset> fixedAssetOpt = fixedAssetRepository.findById(fixedAssetId);
        if (!fixedAssetOpt.isPresent()) {
            throw new IOException("FixedAsset not found with ID: " + fixedAssetId);
        }

        FixedAsset fixedAsset = fixedAssetOpt.get();
        List<FileData> fileDataList = fileDataRepository.findByFixedAsset(fixedAsset);

        List<FileDataDTO> fileDataDTOs = new ArrayList<>();
        for (FileData fileData : fileDataList) {
            Path path = Paths.get(fileData.getFilePath());
            if (Files.exists(path)) {
                FileDataDTO fileDataDTO = new FileDataDTO();
                fileDataDTO.setFileName(fileData.getName());
                fileDataDTO.setFileType(fileData.getType());
                fileDataDTO.setFileContent(Files.readAllBytes(path));
                fileDataDTOs.add(fileDataDTO);
            } else {
                throw new IOException("File not found at path: " + fileData.getFilePath());
            }
        }

        // Create the response DTO
        FixedAssetFileResponseDTO responseDTO = new FixedAssetFileResponseDTO();
responseDTO.setFixedAssetId(fixedAsset.getId());
responseDTO.setFixedAssetName(fixedAsset.getName());
responseDTO.setFixedAssetCategory(fixedAsset.getCategory().getName());
responseDTO.setFixedAssetModel(fixedAsset.getModel());
responseDTO.setFixedAssetYear(fixedAsset.getYear());
responseDTO.setFixedAssetPrice(fixedAsset.getPrice());
responseDTO.setFixedAssetSerialNumber(fixedAsset.getSerialNumber());
responseDTO.setFixedAssetPurchaseDate(fixedAsset.getPurchaseDate());
responseDTO.setFixedAssetUnit(fixedAsset.getUnit());
responseDTO.setFixedAssetQuantity(fixedAsset.getQuantity());
responseDTO.setFixedAssetRemarks(fixedAsset.getRemarks());
responseDTO.setFixedAssetStatus(fixedAsset.getStatus());
responseDTO.setFixedAssetStatusText(fixedAsset.getStatustext());
// responseDTO.setFixedAssetUser(fixedAsset.getUser() != null ? fixedAsset.getUser().getName() : null);
responseDTO.setFixedAssetBuilding(fixedAsset.getBuilding() != null ? fixedAsset.getBuilding().getName() : null);
responseDTO.setFixedAssetAssetHolder(fixedAsset.getAssetHolder() != null ? fixedAsset.getAssetHolder().getName() : null);
         // Adjust based on your FixedAsset entity
        responseDTO.setFiles(fileDataDTOs);
        return responseDTO;
    }

	

}