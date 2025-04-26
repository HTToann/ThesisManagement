/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers.api;

//import com.ts.controllers.*;
import com.ts.services.PdfExportService;
import com.ts.services.StatsService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/secure/stats")
public class ApiStatsController {

    @Autowired
    private StatsService statsService;
    @Autowired
    private PdfExportService pdfService;

    @GetMapping("/thesis")
    public ResponseEntity<?> exportStatsPdf(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if (!"ROLE_MINISTRY".equals(role) && !"ROLE_ADMIN".equals(role)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }

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
//statsService.getGradesSummaryByBoardAndThesis(boardId, thesisId);

    @GetMapping("/pdf/board-summary")
    public ResponseEntity<?> exportBoardSummary(
            @RequestParam("board_id") int boardId,
            @RequestParam("thesis_id") int thesisId,
            HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if (!"ROLE_MINISTRY".equals(role) && !"ROLE_ADMIN".equals(role)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }

        try {
            List<Map<String, Object>> summaries = new ArrayList<>();
            Map<String, Object> row1 = new HashMap<>();
            row1.put("lecturerName", "Nguyễn Văn A");
            row1.put("avgScore", 8.5);
            row1.put("role", "ROLE_CHAIRMAIN");

            Map<String, Object> row2 = new HashMap<>();
            row2.put("lecturerName", "Trần Thị B");
            row2.put("avgScore", 7.8);
            row2.put("role", "ROLE_SECRETARY");

            Map<String, Object> row3 = new HashMap<>();
            row3.put("lecturerName", "Lê Văn C");
            row3.put("avgScore", 9.0);
            row3.put("role", "ROLE_COUNTER");

            summaries.add(row1);
            summaries.add(row2);
            summaries.add(row3);
            int year = java.time.LocalDate.now().getYear();
            String currentDate = java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            byte[] pdfBytes = pdfService.exportBoardSummaryToPdf(summaries, year, currentDate, boardId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("thong_ke_diem_hoi_dong.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
