/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.services.PdfExportService;
import com.ts.services.StatsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/stats")
public class ApiStatsController {

    @Autowired
    private StatsService statsService;
    @Autowired
    private PdfExportService pdfService;

    @GetMapping("/thesis")
    public ResponseEntity<byte[]> exportStatsPdf() {
        try { // 🔹 Dữ liệu mock
            List<Map<String, Object>> mockStats = new ArrayList<>();

            Map<String, Object> row1 = Map.of(
                    "major", "Công nghệ thông tin",
                    "year", 2023,
                    "totalTheses", 12,
                    "avgScore", 8.2
            );
            Map<String, Object> row2 = Map.of(
                    "major", "Khoa học máy tính",
                    "year", 2024,
                    "totalTheses", 9,
                    "avgScore", 7.9
            );
            Map<String, Object> row3 = Map.of(
                    "major", "Hệ thống thông tin",
                    "year", 2025,
                    "totalTheses", 14,
                    "avgScore", 8.6
            );

            mockStats.add(row1);
            mockStats.add(row2);
            mockStats.add(row3);
//            List<Map<String, Object>> stats = statsService.getStatsByMajorAndYear();
            byte[] pdf = pdfService.exportStatsToPdf(mockStats);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename("thong_ke_khoa_luan.pdf").build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
