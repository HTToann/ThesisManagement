/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Criteria;
import com.ts.pojo.Faculty;
import com.ts.services.FacultyService;
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
public class FacultyController {
    
    @Autowired
    private FacultyService service;
    @GetMapping("/admin/faculty")
     public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Faculty> faculty;
        if (keyword == null || keyword.isEmpty()) {
            faculty = service.getAll();
        } else {
            List<Faculty> f = service.searchByName(keyword);
            faculty = f != null ? f : new ArrayList<>(); 
        }
        model.addAttribute("faculty", faculty);
        return "faculty";
    }
}
