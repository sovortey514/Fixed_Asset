package com.twd.SpringSecurityJWT.service;

import com.twd.SpringSecurityJWT.entity.FixedAsset;
import com.twd.SpringSecurityJWT.repository.FixedAssetRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    public ByteArrayOutputStream exportFixedAssetsToExcel() throws IOException {
        List<FixedAsset> fixedAssets = fixedAssetRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Fixed Assets");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Category", "Name", "Model", "Year", "Price", "Serial Number", "Purchase Date", "Unit", "Quantity", "Remarks", "Status", "Status Text", "User", "Building", "Image"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Create data rows
        int rowNum = 1;
        for (FixedAsset asset : fixedAssets) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(asset.getId());
            row.createCell(1).setCellValue(asset.getCategory() != null ? asset.getCategory().getName() : "N/A");
            row.createCell(2).setCellValue(asset.getName());
            row.createCell(3).setCellValue(asset.getModel());
            row.createCell(4).setCellValue(asset.getYear());
            row.createCell(5).setCellValue(asset.getPrice());
            row.createCell(6).setCellValue(asset.getSerialNumber());
            row.createCell(7).setCellValue(asset.getPurchaseDate() != null ? asset.getPurchaseDate().toString() : "N/A");
            row.createCell(8).setCellValue(asset.getUnit());
            row.createCell(9).setCellValue(asset.getQuantity());
            row.createCell(10).setCellValue(asset.getRemarks() != null ? asset.getRemarks() : "N/A");
            row.createCell(11).setCellValue(asset.getStatus() != null ? asset.getStatus() : "N/A");
            row.createCell(12).setCellValue(asset.getStatustext());
            row.createCell(13).setCellValue(asset.getUser() != null ? asset.getUser().getName() : "N/A");
            row.createCell(14).setCellValue(asset.getBuilding() != null ? asset.getBuilding().getName() : "N/A");
            row.createCell(15).setCellValue(asset.getImage() != null ? "Image Data" : "No Image"); // Adjust as needed
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }
}
