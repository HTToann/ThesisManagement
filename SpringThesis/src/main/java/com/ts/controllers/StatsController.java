/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Major;
import com.ts.services.StatsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class StatsController {
    
    @Autowired
    private StatsService statsService;
    @GetMapping("/admin/stats")
     public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Map<String, Object>> stats = statsService.getStatsByMajorAndYear();
        model.addAttribute("stats", stats);
        return "stats-report";
    }
}
