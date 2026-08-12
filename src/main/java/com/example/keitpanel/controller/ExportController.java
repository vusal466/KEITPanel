package com.example.keitpanel.controller;

import com.example.keitpanel.services.impl.ExportServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exports")
@RequiredArgsConstructor
@Tag(name = "Export", description = "Excel İxrac Əməliyyatları")
public class ExportController {

    private final ExportServiceImpl exportService;

    @GetMapping("/equipments")
    public ResponseEntity<byte[]> exportEquipments() {
        byte[] excelContent = exportService.exportEquipments();
        return createExcelResponse(excelContent, "equipments.xlsx");
    }

    @GetMapping("/branches")
    public ResponseEntity<byte[]> exportBranches() {
        byte[] excelContent = exportService.exportBranches();
        return createExcelResponse(excelContent, "branches.xlsx");
    }

    @GetMapping("/assignments")
    public ResponseEntity<byte[]> exportAssignments() {
        byte[] excelContent = exportService.exportAssignments();
        return createExcelResponse(excelContent, "assignments.xlsx");
    }

    private ResponseEntity<byte[]> createExcelResponse(byte[] content, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(content);
    }
}