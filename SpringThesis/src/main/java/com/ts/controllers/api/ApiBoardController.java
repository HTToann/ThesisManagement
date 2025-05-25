    package com.ts.controllers.api;

import com.ts.pojo.Board;
import com.ts.pojo.BoardRequestDTO;
import com.ts.services.BoardService;
import com.ts.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiBoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<?> listBoards() {
        try {
            return ResponseEntity.ok(boardService.getAllBoard());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @PostMapping("/secure/boards")
    public ResponseEntity<?> createBoard() {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền tạo hội đồng."));
        }
        try {
            Board board = boardService.addBoard(new Board());
            return new ResponseEntity<>(board, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }
    @PostMapping("/secure/add/board-members")
    public ResponseEntity<?> createBoardAndMembers(@RequestBody BoardRequestDTO request) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền tạo hội đồng."));
        }
        try {
            Board board = boardService.createBoardWithMembers(request);
            return new ResponseEntity<>(board, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @PatchMapping("/secure/boards/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable("id") int id,
                                         @RequestBody Map<String, String> payload) {
        if (!AuthUtils.hasAnyRole("ROLE_ADMIN", "ROLE_MINISTRY")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Bạn không có quyền cập nhật hội đồng."));
        }
        try {
            Board b = boardService.updateBoard(id, payload);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã có lỗi xảy ra"));
        }
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<?> getBoardById(@PathVariable("id") int id) {
        try {
            Board b = boardService.getBoardById(id);
            if (b == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(b);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }
}
