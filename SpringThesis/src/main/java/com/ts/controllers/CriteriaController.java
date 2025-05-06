/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Criteria;
import com.ts.pojo.Users;
import com.ts.services.CriteriaService;
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
public class CriteriaController {
    
    @Autowired
    private CriteriaService service;
    
    @GetMapping("/admin/criteria")
     public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Criteria> criteria;
        if (keyword == null || keyword.isEmpty()) {
            criteria = service.getAll();
        } else {
            List<Criteria> c = service.searchByName(keyword);
            criteria = c != null ? c : new ArrayList<>(); 
        }
        model.addAttribute("criteria", criteria);
        return "criteria";
    }
}
