/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Student;
import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisLecturer;
import com.ts.services.BoardService;
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
public class ThesisController {

    @Autowired
    private ThesisService thesisService;
    @Autowired
    private BoardService boardService;

    @GetMapping("/admin/thesis")
    public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<Thesis> thesis;
        if (keyword == null || keyword.isEmpty()) {
            thesis = thesisService.getAllThesis();
        } else {
            try {
                List<Thesis> t = thesisService.getThesisByName(keyword);
                thesis = (t != null) ? t : new ArrayList<>();  // ✅ đảm bảo không null
            } catch (NumberFormatException ex) {
                thesis = new ArrayList<>();  // ✅ nếu keyword không phải số
            }
        }

        model.addAttribute("theses", thesis);
        model.addAttribute("boards", boardService.getAllBoard());
        return "thesis";
    }
}
