package com.ts.controllers.api;

import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisMemberInfoDTO;
import com.ts.services.ThesisMemberService;
import com.ts.services.ThesisService;
import com.ts.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiThesisController {

    @Autowired
    private ThesisService thesisService;
    @Autowired
    private ThesisMemberService thesisMemberSerivce;

    @GetMapping("/thesis")
    public ResponseEntity<?> getAllTheses() {
        return ResponseEntity.ok(thesisService.getAllThesis());
    }

    @GetMapping("/theses/{userId}")
    public ResponseEntity<?> getThesesForUser(@PathVariable("userId") int userId) {
        try {
            List<Thesis> result = thesisService.getThesesByUserId(userId);
            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy khóa luận cho userId: " + userId));
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @GetMapping("/thesis-members/{id}")
    public ResponseEntity<?> getThesisMemberById(@PathVariable("id") int id) {
        try {
            List<ThesisMemberInfoDTO> t = thesisMemberSerivce.getThesisMembersByThesisId(id);
            if (t == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy đề tài.");
            }
            return ResponseEntity.ok(t);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }

    @GetMapping("/search-thesis")
    public ResponseEntity<?> getThesisByName(@RequestParam("keyword") String keyword) {
        try {
            List<Thesis> t = thesisService.getThesisByName(keyword);
            if (t == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy.");
            }
            return ResponseEntity.ok(t);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
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
    public ResponseEntity<?> createThesis(@RequestBody Map<String, String> payload, Principal principal) {
//        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
//        }
        try {
            return new ResponseEntity<>(thesisService.addThesis(payload, principal), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @PutMapping("/secure/thesis/{id}")
    public ResponseEntity<?> updateThesis(@PathVariable("id") int id, @RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
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

    @PatchMapping("/secure/thesis/{id}")
    public ResponseEntity<?> updateBrowsing(@PathVariable("id") int id, @RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền"));
        }
        try {
            return ResponseEntity.ok(thesisService.updateBrowsingThesis(id, payload));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @DeleteMapping("/secure/thesis/{id}")
    public ResponseEntity<?> deleteThesis(@PathVariable("id") int id) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
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
