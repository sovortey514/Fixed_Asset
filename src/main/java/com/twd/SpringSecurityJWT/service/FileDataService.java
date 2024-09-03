package com.twd.SpringSecurityJWT.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
public interface FileDataService {
    String uploadFileToFileDirectory(MultipartFile file) throws IOException;

	byte[] downloadFileFromFileDirectory(String fileName) throws IOException;
}
