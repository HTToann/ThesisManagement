/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Faculty;
import com.ts.pojo.Major;
import com.ts.services.FacultyService;
import com.ts.services.MajorService;
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
public class MajorController {
    @Autowired
    private MajorService service;
    @Autowired
    private FacultyService falService;
    @GetMapping("/admin/major")
     public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Major> major;
        if (keyword == null || keyword.isEmpty()) {
            major = service.getAllMajors();
        } else {
            List<Major> m = service.searchByName(keyword);
            major = m != null ? m : new ArrayList<>(); 
        }
        model.addAttribute("majors", major);
        model.addAttribute("facultyList",falService.getAll());
        return "major";
    }
}
