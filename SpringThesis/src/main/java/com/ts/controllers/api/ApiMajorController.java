/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers.api;

//import com.ts.controllers.*;
import com.ts.pojo.Major;
import com.ts.services.MajorService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiMajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping("/majors")
    public ResponseEntity<?> getAllMajors() {
        return ResponseEntity.ok(majorService.getAllMajors());
    }

    @GetMapping("/majors/{id}")
    public ResponseEntity<?> getMajorById(@PathVariable("id") int id) {
        try {
            Major major = majorService.getById(id);
            if (major == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(major);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @GetMapping("/majors/search")
    public ResponseEntity<?> getMajorByName(@RequestParam String name) {
        try {
            Major major = majorService.getByName(name);
            if (major == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(major);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @PostMapping("/secure/majors")
    public ResponseEntity<?> createMajor(@RequestBody Map<String, String> payload,HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if (!"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            return ResponseEntity.ok(majorService.addMajor(payload));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }

    @PutMapping("/secure/majors/{id}")
    public ResponseEntity<?> updateMajor(@PathVariable("id") int id, @RequestBody Map<String, String> payload,HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if (!"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            Major existing = majorService.getById(id);
            if (existing == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(majorService.updateMajor(id, payload));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }

    @DeleteMapping("/secure/majors/{id}")
    public ResponseEntity<?> deleteMajor(@PathVariable int id,HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if (!"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            Major existing = majorService.getById(id);
            if (existing == null) {
                return ResponseEntity.notFound().build();
            }
            majorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }
}
