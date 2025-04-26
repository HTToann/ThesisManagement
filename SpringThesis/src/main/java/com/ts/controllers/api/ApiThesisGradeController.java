/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers.api;

//import com.ts.controllers.*;
import com.ts.pojo.ThesisGrade;
import com.ts.services.ThesisGradeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/secure/thesis-grades")
@CrossOrigin
public class ApiThesisGradeController {

    @Autowired
    private ThesisGradeService thesisGradeService;

    // POST /api/thesis-grades: Ghi điểm
    @PostMapping
    public ResponseEntity<?> addGrade(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_LECTURER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            thesisGradeService.add(payload);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }

    // PUT /api/thesis-grades: Cập nhật điểm
    @PutMapping
    public ResponseEntity<?> updateGrade(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_LECTURER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            thesisGradeService.update(payload);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }

    // GET /api/thesis-grades?thesis_id=1
    @GetMapping(params = "thesis_id")
    public ResponseEntity<?> getByThesisId(@RequestParam("thesis_id") int thesisId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_LECTURER".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            List<ThesisGrade> thesis = this.thesisGradeService.getByThesisId(thesisId);
            if (thesis == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(thesis);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }

    // GET /api/thesis-grades?lecturer_id=...&board_id=...
    @GetMapping(params = {"lecturer_id", "board_id"})
    public ResponseEntity<?> getByLecturerAndBoard(@RequestParam("lecturer_id") int lecturerId,
            @RequestParam("board_id") int boardId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_LECTURER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            List<ThesisGrade> thesis = this.thesisGradeService.getByLecturerAndBoard(lecturerId, boardId);
            if (thesis == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(thesis);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteGrade(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_LECTURER".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            this.thesisGradeService.delete(payload);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }
}
