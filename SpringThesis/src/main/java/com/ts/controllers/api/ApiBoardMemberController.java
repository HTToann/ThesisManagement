package com.ts.controllers.api;

import com.ts.pojo.BoardMember;
import com.ts.services.BoardMemberService;
import com.ts.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiBoardMemberController {

    @Autowired
    private BoardMemberService boardMemberService;

    @PatchMapping("/secure/board-members/{boardId}/{lecturerId}")
    public ResponseEntity<?> updateBoardMemberRole(
            @PathVariable("boardId") int boardId,
            @PathVariable("lecturerId") int lecturerId,
            @RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền."));
        }
        try {
            boardMemberService.updateRole(boardId, lecturerId, payload);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Có lỗi xảy ra."));
        }
    }

    @PostMapping("/secure/board-members")
    public ResponseEntity<?> addBoardMember(@RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền."));
        }
        try {
            BoardMember member = boardMemberService.add(payload);
            return new ResponseEntity<>(member, HttpStatus.CREATED);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "ID không hợp lệ"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi khi thêm thành viên vào hội đồng"));
        }
    }

    @GetMapping("/board-members/board_id/{id}")
    public ResponseEntity<?> listMemberOfBoardById(@PathVariable("id") int boardId) {
        try {
            List<BoardMember> bm = boardMemberService.getBoardMembersByBoardId(boardId);
            if (bm == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(bm);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "ID không hợp lệ"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi!!"));
        }
    }

    @DeleteMapping("/secure/board-members/{boardId}/{lecturerId}")
    public ResponseEntity<?> deleteBoardMember(@PathVariable("boardId") int boardId,
            @PathVariable("lecturerId") int lecturerId) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền."));
        }
        try {
            boardMemberService.removeBoardMember(boardId, lecturerId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể xóa giảng viên khỏi hội đồng."));
        }
    }
}
