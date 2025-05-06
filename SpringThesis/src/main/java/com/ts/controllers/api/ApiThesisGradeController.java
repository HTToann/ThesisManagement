package com.ts.controllers.api;

import com.ts.pojo.ThesisGrade;
import com.ts.services.ThesisGradeService;
import com.ts.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure/thesis-grades")
@CrossOrigin
public class ApiThesisGradeController {

    @Autowired
    private ThesisGradeService thesisGradeService;

    // 📌 Ghi điểm
    @PostMapping
    public ResponseEntity<?> addGrade(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_LECTURER")) {
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

    // 📌 Cập nhật điểm
    @PutMapping
    public ResponseEntity<?> updateGrade(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_LECTURER")) {
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

    // 📌 GET theo thesis_id
    @GetMapping(params = "thesis_id")
    public ResponseEntity<?> getByThesisId(@RequestParam("thesis_id") int thesisId) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_LECTURER", "ROLE_MINISTRY")) {
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

    // 📌 GET theo lecturer_id + board_id
    @GetMapping(params = {"lecturer_id", "board_id"})
    public ResponseEntity<?> getByLecturerAndBoard(@RequestParam("lecturer_id") int lecturerId,
                                                   @RequestParam("board_id") int boardId) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_LECTURER")) {
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

    // 📌 Xoá điểm
    @DeleteMapping
    public ResponseEntity<?> deleteGrade(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_LECTURER", "ROLE_MINISTRY")) {
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
