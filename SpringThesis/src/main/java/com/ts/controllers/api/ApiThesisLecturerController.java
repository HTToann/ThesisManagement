/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers.api;

//import com.ts.controllers.*;
import com.ts.pojo.ThesisLecturer;
import com.ts.services.ThesisLecturerService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/secure/thesis-lecturers")
@CrossOrigin
public class ApiThesisLecturerController {

    @Autowired
    private ThesisLecturerService thesisLecturerService;
    // 📌 Thêm giảng viên hướng dẫn

    @PostMapping
    public ResponseEntity<?> addLecturer(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            thesisLecturerService.add(payload);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }

    // 📌 Lấy danh sách giảng viên theo đề tài
    @GetMapping("/{thesisId}")
    public ResponseEntity<?> getLecturers(@PathVariable("thesisId") int thesisId, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if ("ROLE_STUDENT".equals(role) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            List<ThesisLecturer> thesisLecturer = this.thesisLecturerService.getByThesisId(thesisId);
            if (thesisLecturer == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(thesisLecturer);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }

    }

    // 📌 Xóa giảng viên hướng dẫn
    @DeleteMapping
    public ResponseEntity<?> deleteLecturer(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            thesisLecturerService.delete(payload);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }

    // 📌 Cập nhật thay giảng viên hướng dẫn
    @PatchMapping
    public ResponseEntity<?> updateLecturer(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            thesisLecturerService.updateLecturer(payload);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Đã xảy ra lỗi"));
        }
    }
}
