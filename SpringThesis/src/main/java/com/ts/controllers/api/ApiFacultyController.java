package com.ts.controllers.api;

import com.ts.pojo.Faculty;
import com.ts.services.FacultyService;
import com.ts.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiFacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/faculty/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            Faculty f = this.facultyService.getById(id);
            if (f == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(f);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @GetMapping("/faculty/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(facultyService.getAll());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }

    @PostMapping("/secure/faculty/create")
    public ResponseEntity<?> create(@RequestBody Map<String, String> faculty) {
        if (!AuthUtils.hasRole("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }

        try {
            return ResponseEntity.ok(facultyService.create(faculty));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }

    @PutMapping("/secure/faculty/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Map<String, String> faculty) {
        if (!AuthUtils.hasRole("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }

        try {
            return ResponseEntity.ok(facultyService.update(id, faculty));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }

    @DeleteMapping("/secure/faculty/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        if (!AuthUtils.hasRole("ROLE_ADMIN")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }

        try {
            facultyService.delete(id);
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
