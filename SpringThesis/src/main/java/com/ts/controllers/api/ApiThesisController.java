/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers.api;

//import com.ts.controllers.*;
import com.ts.pojo.Thesis;
import com.ts.services.ThesisService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api")
@CrossOrigin
public class ApiThesisController {

    @Autowired
    private ThesisService thesisService;

    @GetMapping("/thesis")
    public ResponseEntity<?> getAllTheses() {
        return ResponseEntity.ok(thesisService.getAllThesis());
    }

    @GetMapping("/thesis/{id}")
    public ResponseEntity<?> getThesisById(@PathVariable("id") int id) {
        try {
            Thesis t = thesisService.getThesisById(id);
            if (t == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đề tài.");
            }
            return ResponseEntity.ok(t);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @PostMapping("/secure/thesis")
    public ResponseEntity<?> createThesis(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            return new ResponseEntity<>(thesisService.addThesis(payload), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @PutMapping("/secure/thesis/{id}")
    public ResponseEntity<?> updateThesis(@PathVariable("id") int id, @RequestBody Map<String, String> payload, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            return ResponseEntity.ok(thesisService.updateThesis(id, payload));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @DeleteMapping("/secure/thesis/{id}")
    public ResponseEntity<?> deleteThesis(@PathVariable("id") int id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role) && !"ROLE_MINISTRY".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            this.thesisService.deleteThesisById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

}
