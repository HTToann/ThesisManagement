/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Board;
import com.ts.services.BoardService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class ApiBoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/boards")
    public ResponseEntity<?> listBoards() {
        return ResponseEntity.ok(this.boardService.getAllBoard());
    }

    @PostMapping("/boards")
    public ResponseEntity<?> createBoard() {
        Board board = new Board();
        board = this.boardService.addBoard(board);  // Thêm board vào DB
        return new ResponseEntity<>(board, HttpStatus.CREATED);  // Trả về chính đối tượng vừa tạo
    }

    @PatchMapping("/boards/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable("id") int id,
            @RequestParam Map<String, String> params) {
        try {
            Board b = this.boardService.updateBoard(id, params);
            return ResponseEntity.ok(b);
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
            return new ResponseEntity<>(this.boardService.getBoardById(id), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi."));
        }
    }
}

