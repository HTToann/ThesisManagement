/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Board;
import com.ts.pojo.Faculty;
import com.ts.services.BoardService;
import com.ts.services.FacultyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Lenovo
 */
@Controller
public class BoardController {
    @Autowired
    private BoardService service;
    @GetMapping("/admin/board")
     public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Board> board;     
        if (keyword == null || keyword.isEmpty()) {
            board = service.getAllBoard();
        } else {
            int kwInt=Integer.parseInt(keyword.trim());
            Board b = service.getBoardById(kwInt);
            board = b != null ? List.of(b) : List.of(); 
        }
        model.addAttribute("boards", board);
        return "board";
    }
}
