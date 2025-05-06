/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Board;
import com.ts.pojo.BoardMember;
import com.ts.services.BoardMemberService;
import com.ts.services.BoardService;
import com.ts.services.UsersService;
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
public class BoardMemberController {
    @Autowired
    private BoardMemberService service;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UsersService usersService;
    @GetMapping("/admin/board-members")
    public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<BoardMember> boardMembers = null;     
        if (keyword == null || keyword.isEmpty()) {
            boardMembers = service.getAll();
        } else {
            int kwInt=Integer.parseInt(keyword.trim());
            List<BoardMember> b = service.getBoardMembersByBoardId(kwInt);
            if(b!= null) 
                boardMembers = b;
        }
        model.addAttribute("boardMembers", boardMembers);
        model.addAttribute("boards",boardService.getAllBoard());
        model.addAttribute("lecturers",usersService.getAllUsersRoleLecturer());
        return "boardMembers";
           
    }
}
