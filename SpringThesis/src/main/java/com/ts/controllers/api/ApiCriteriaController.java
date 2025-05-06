package com.ts.controllers.api;

import com.ts.pojo.Criteria;
import com.ts.services.CriteriaService;
import com.ts.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCriteriaController {

    @Autowired
    private CriteriaService criteriaService;

    @GetMapping("/criteria")
    public ResponseEntity<?> getAllCriteria() {
        return ResponseEntity.ok(criteriaService.getAll());
    }

    @PostMapping("/secure/criteria")
    public ResponseEntity<?> createCriteria(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền."));
        }
        try {
            Criteria c = criteriaService.add(payload);
            return new ResponseEntity<>(c, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }

    @PutMapping("/secure/criteria/{id}")
    public ResponseEntity<?> updateCriteria(@PathVariable("id") int id, @RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền."));
        }
        try {
            Criteria updated = criteriaService.update(id, payload);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }

    @DeleteMapping("/secure/criteria/{id}")
    public ResponseEntity<?> deleteCriteria(@PathVariable("id") int id) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền."));
        }
        try {
            criteriaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi không xác định."));
        }
    }
}
