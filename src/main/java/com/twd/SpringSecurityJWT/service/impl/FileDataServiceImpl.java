package com.twd.SpringSecurityJWT.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.twd.SpringSecurityJWT.entity.FileData;
import com.twd.SpringSecurityJWT.repository.FileDataRepository;
import com.twd.SpringSecurityJWT.service.FileDataService;

@Service
public class FileDataServiceImpl implements FileDataService{
	
	@Autowired
	private FileDataRepository fileDataRepository;
	
	
	private final String FILE_PATH = "D:\\Year4\\project_Intern\\Fixed_Asset\\src\\Uploads\\";

    @Override
	public String uploadFileToFileDirectory(MultipartFile file) throws IOException {
		String filePath = FILE_PATH+file.getOriginalFilename();//absolute path
		
		FileData fileData = fileDataRepository.save(FileData.builder()
				.name(file.getOriginalFilename())
				.type(file.getContentType())
				.filePath(filePath).build());
		
		//copy your file into that particular path
		file.transferTo(new java.io.File(filePath)); 
		
		if(fileData!= null) {
				return "file uploaded successfully : "+file.getOriginalFilename()+ " and Files uploaded path is :"+filePath;
		}
		return null;
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

	

}