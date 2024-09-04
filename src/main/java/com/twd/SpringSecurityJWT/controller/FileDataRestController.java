package com.twd.SpringSecurityJWT.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.twd.SpringSecurityJWT.dto.FixedAssetFileResponseDTO;
import com.twd.SpringSecurityJWT.service.FileDataService;

@RestController
@RequestMapping("/admin")
public class FileDataRestController {

    @Autowired
    private FileDataService fileDataService;

    @PostMapping("/upload_image")
    public ResponseEntity<?> uploadImageToFileDirectory(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fixedAssetId") Long fixedAssetId) throws IOException {

        // Call the service method with both file and fixedAssetId
        String uploadFile = fileDataService.uploadFileToFileDirectory(file, fixedAssetId);

        return ResponseEntity.status(HttpStatus.OK).body(uploadFile);
    }

    @GetMapping("/get_images_by_asset/{fixedAssetId}")
    public ResponseEntity<?> downloadAllImagesByFixedAssetId(@PathVariable Long fixedAssetId) {
        try {
            FixedAssetFileResponseDTO files = fileDataService.downloadAllFilesByFixedAssetId(fixedAssetId);
            return ResponseEntity.status(HttpStatus.OK).body(files);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading files: " + e.getMessage());
        }
    }

    @GetMapping("/get_image/{fileName}")
    public ResponseEntity<?> downloadImageFromFileDirectory(@PathVariable String fileName) {
        try {
            byte[] downloadFile = fileDataService.downloadFileFromFileDirectory(fileName);

            MediaType mediaType;
            if (fileName.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else {
                mediaType = MediaType.APPLICATION_OCTET_STREAM; // Fallback
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(mediaType)
                    .body(downloadFile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading file: " + e.getMessage());
        }
    }

}