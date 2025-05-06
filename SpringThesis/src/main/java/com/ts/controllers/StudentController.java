/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Major;
import com.ts.pojo.Student;
import com.ts.services.MajorService;
import com.ts.services.StudentService;
import com.ts.services.ThesisService;
import com.ts.services.UsersService;
import java.util.ArrayList;
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
public class StudentController {
    
    @Autowired
    private StudentService service;
    @Autowired
    private MajorService majorService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ThesisService thesisService;
    @GetMapping("/admin/student")
     public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Student> student;
        if (keyword == null || keyword.isEmpty()) {
            student = service.getAll();
        } else {
            List<Student> s = service.getStudentsByUsername(keyword);
            student = (s!= null) ? s : new ArrayList<>();
        }
        
        model.addAttribute("students", student);
        model.addAttribute("majors",this.majorService.getAllMajors());
        model.addAttribute("users",this.usersService.getAllUsersRoleStudent());
        model.addAttribute("theses",this.thesisService.getAllThesis());
        return "student";
    }
}
