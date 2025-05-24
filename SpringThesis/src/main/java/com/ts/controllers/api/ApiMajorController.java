package com.ts.controllers.api;

import com.ts.pojo.Major;
import com.ts.services.MajorService;
import com.ts.utils.AuthUtils;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/search-major")
    public ResponseEntity<?> getMajorByName(@RequestParam("keyword") String keyword) {
        try {
            List<Major> t = majorService.searchByName(keyword);
            if (t == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy.");
            }
            return ResponseEntity.ok(t);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
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



    @PostMapping("/secure/majors")
    public ResponseEntity<?> createMajor(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
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
    public ResponseEntity<?> updateMajor(@PathVariable("id") int id, @RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasRole("ROLE_MINISTRY")) {
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
    public ResponseEntity<?> deleteMajor(@PathVariable("id") int id) {
        if (!AuthUtils.hasRole("ROLE_MINISTRY")) {
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
