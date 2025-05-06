/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.ThesisLecturer;
import com.ts.services.ThesisLecturerService;
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
public class ThesisLecturerController {

    @Autowired
    private ThesisLecturerService service;
    @Autowired
    private ThesisService thesisService;
    @Autowired
    private UsersService userService;

    @GetMapping("/admin/thesis-lecturer")
    public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<ThesisLecturer> thesisLecturer;
        if (keyword == null || keyword.isEmpty()) {
            thesisLecturer = this.service.getAll();
        } else {
            try {
                List<ThesisLecturer> tg = service.getByThesisName(keyword);
                thesisLecturer = (tg != null) ? tg : new ArrayList<>();  // ✅ đảm bảo không null
            } catch (NumberFormatException ex) {
                thesisLecturer = new ArrayList<>();  // ✅ nếu keyword không phải số
            }
        }
        model.addAttribute("thesisLecturers", thesisLecturer);
        model.addAttribute("theses", thesisService.getAllThesis());
        model.addAttribute("lecturers", userService.getAllUsersRoleLecturer());
        return "thesisLecturer";
    }
}
