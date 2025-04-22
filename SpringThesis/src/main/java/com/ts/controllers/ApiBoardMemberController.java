/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Board;
import com.ts.pojo.BoardMember;
import com.ts.pojo.BoardMemberPK;
import com.ts.pojo.Users;
import com.ts.services.BoardMemberService;
import com.ts.services.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.ts.services.UsersService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiBoardMemberController {

    @Autowired
    private BoardMemberService boardMemberService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UsersService usersService;

    @PostMapping("/board-members")
    public ResponseEntity<?> addBoardMember(@RequestBody Map<String, String> payload) {
        try {
            int boardId = Integer.parseInt(payload.get("boardId"));
            int lecturerId = Integer.parseInt(payload.get("lecturerId"));
            String role = payload.get("roleInBoard");

            // Lấy thực thể Board và Users từ ID
            Board board = boardService.getBoardById(boardId);
            Users lecturer = usersService.getUserById(lecturerId);

            if (board == null || lecturer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Board hoặc Lecturer không tồn tại"));
            }

            // Tạo đối tượng BoardMember
            BoardMember member = new BoardMember();
            member.setBoard(board);
            member.setUsers(lecturer);
            member.setRoleInBoard(role);
            member.setBoardMemberPK(new BoardMemberPK(boardId, lecturerId));

            // Gọi service để kiểm tra & lưu
            boardMemberService.addBoardMember(member);

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
            return new ResponseEntity<>(this.boardMemberService.getBoardMembersByBoardId(boardId), HttpStatus.OK);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "ID không hợp lệ"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi!!"));
        }
    }

    // DELETE /api/board-members?boardId=1&lecturerId=2
    @DeleteMapping("board-members")
    public ResponseEntity<?> deleteBoardMember(@RequestParam("boardId") int boardId,
            @RequestParam("lecturerId") int lecturerId) {
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
