package com.ts.controllers.api;

import com.ts.pojo.ThesisLecturer;
import com.ts.services.ThesisLecturerService;
import com.ts.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure/thesis-lecturers")
@CrossOrigin
public class ApiThesisLecturerController {

    @Autowired
    private ThesisLecturerService thesisLecturerService;

    @PostMapping
    public ResponseEntity<?> addLecturer(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasRole("ROLE_ADMIN") && !AuthUtils.hasRole("ROLE_MINISTRY")) {
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

    @GetMapping("/{thesisId}")
    public ResponseEntity<?> getLecturers(@PathVariable("thesisId") int thesisId) {
        if (AuthUtils.hasRole("ROLE_STUDENT")) {
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

    @DeleteMapping
    public ResponseEntity<?> deleteLecturer(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasRole("ROLE_ADMIN") && !AuthUtils.hasRole("ROLE_MINISTRY")) {
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

    @PatchMapping
    public ResponseEntity<?> updateLecturer(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasRole("ROLE_ADMIN") && !AuthUtils.hasRole("ROLE_MINISTRY")) {
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
