package com.twd.SpringSecurityJWT.controller;

import java.io.IOException;

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

import com.twd.SpringSecurityJWT.service.FileDataService;

@RestController
@RequestMapping("/admin")
public class FileDataRestController {
	

	
	@Autowired
	private FileDataService fileDataService;
	
	@PostMapping("/upload_image")
	public ResponseEntity<?> uploadImageToFileDirectory(@RequestParam("file") MultipartFile file) throws IOException{
		String uploadFile = fileDataService.uploadFileToFileDirectory(file);
		
		return ResponseEntity.status(HttpStatus.OK).body(uploadFile);
		
		 
	}
	
	@GetMapping("/get_image/{fileName}")
    public ResponseEntity<?> downloadImageFromFileDirectory(@PathVariable String fileName) {
        try {
            byte[] downloadFile = fileDataService.downloadFileFromFileDirectory(fileName);
            
            
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM; 
            if (fileName.endsWith(".png")) {
                mediaType = MediaType.IMAGE_PNG;
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
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