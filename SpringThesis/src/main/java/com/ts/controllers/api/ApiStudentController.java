/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers.api;

//import com.ts.controllers.*;
import com.ts.pojo.Student;
import com.ts.pojo.Users;
import com.ts.services.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
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

    @PatchMapping("/student/{id}/thesis")
    public ResponseEntity<?> updateThesis(@PathVariable("id") int id, @RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if (!"ROLE_MINISTRY".equals(role) && !"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            //payload phải chứa 1 là major 2 là thesisId
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
    public ResponseEntity<?> deleteStudentByUserId(@PathVariable("id") int id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");

        if (!"ROLE_MINISTRY".equals(role) && !"ROLE_ADMIN".equals(role)) {
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
