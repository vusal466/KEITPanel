package com.example.keitpanel.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] generateExcel(String sheetName, List<String> headers, List<List<Object>> dataRows) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // 1. Başlıq Sətirinin Stili
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 2. Başlıqların Yazılması
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // 3. Data Sətirlərinin Doldurulması
            int rowIdx = 1;
            for (List<Object> rowData : dataRows) {
                Row row = sheet.createRow(rowIdx++);
                for (int colIdx = 0; colIdx < rowData.size(); colIdx++) {
                    Cell cell = row.createCell(colIdx);
                    Object val = rowData.get(colIdx);

                    if (val != null) {
                        cell.setCellValue(val.toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            // 4. Sütun Genişliklərinin Avtomatik Tənzimlənməsi
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Excel faylı generasiya olunarkən xəta baş verdi", e);
        }
    }
}