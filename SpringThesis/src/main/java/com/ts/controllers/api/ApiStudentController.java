package com.ts.controllers.api;

import com.ts.pojo.Student;
import com.ts.services.StudentService;
import com.ts.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
@CrossOrigin
public class ApiStudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/student/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") int id) {
        try {
            Student s = service.getStudentByUserId(id);
            if (s == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User không tồn tại!"));
            }
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @GetMapping("/student-thesis/{id}")
    public ResponseEntity<?> getStudentByThesis(@PathVariable("id") int id) {
        try {
            List<Student> students = service.getStudentsByThesisId(id);
            if (students.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy sinh viên cho đề tài có ID = " + id));
            }
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi: " + e.getMessage()));
        }
    }

    @GetMapping("/search-student")
    public ResponseEntity<?> getStudentByName(@RequestParam("keyword") String keyword) {
        try {
            List<Student> t = service.getStudentsByUsername(keyword);
            if (t == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy.");
            }
            return ResponseEntity.ok(t);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @PostMapping("/student")
    public ResponseEntity<?> addStudent(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }

        try {
            service.insertStudent(payload);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể tạo sinh viên"));
        }
    }

    @PatchMapping("/student/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id,
            @RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_MINISTRY", "ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            Student s = service.updateStudent(id, payload);
            return new ResponseEntity<>(s, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi!!"));
        }
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<?> deleteStudentByUserId(@PathVariable("id") int id) {
        if (!AuthUtils.hasAnyRole("ROLE_MINISTRY", "ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            this.service.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi!!"));
        }
    }
}
