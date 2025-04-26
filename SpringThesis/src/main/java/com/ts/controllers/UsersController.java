/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Users;
import com.ts.services.FacultyService;
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
public class UsersController {

    @Autowired
    private UsersService service;
    @Autowired
    private FacultyService facService;
    @GetMapping("/admin/users")
    public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Users> users;
        if (keyword == null || keyword.isEmpty()) {
            users = service.getAllUsers();
        } else {
            Users u = service.getUserByUsername(keyword);
            users = u != null ? List.of(u) : List.of(); // Java 9+ | Arrays.asList(u) nếu Java 8
        }
        model.addAttribute("users", users);
        model.addAttribute("faculty",this.facService.getAll());
        return "users";
    }

}
