/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.controllers;

import com.ts.pojo.Thesis;
import com.ts.pojo.ThesisGrade;
import com.ts.services.BoardService;
import com.ts.services.CriteriaService;
import com.ts.services.ThesisGradeService;
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
public class ThesisGradeController {

    @Autowired
    private ThesisGradeService thesisGradeService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private ThesisService thesisService;
    @Autowired
    private UsersService userService;
    @GetMapping("/admin/thesis-grade")
    public String addView(Model model, @RequestParam(value = "kw", required = false) String keyword) {
        List<ThesisGrade> thesisGrade;
        if (keyword == null || keyword.isEmpty()) {
            thesisGrade = this.thesisGradeService.getAll();
        } else {
            try {
                int kwInt = Integer.parseInt(keyword);
                List<ThesisGrade> tg = thesisGradeService.getByBoardId(kwInt);
                thesisGrade = (tg != null) ? tg : new ArrayList<>();  // ✅ đảm bảo không null
            } catch (NumberFormatException ex) {
                thesisGrade = new ArrayList<>();  // ✅ nếu keyword không phải số
            }
        }
        model.addAttribute("grades", thesisGrade);
        model.addAttribute("boards", this.boardService.getAllBoard());
        model.addAttribute("theses", this.thesisService.getAllThesis());
        model.addAttribute("lecturers", this.userService.getAllUsersRoleLecturer());
         model.addAttribute("criteriaList", this.criteriaService.getAll());
        return "thesisGrade";
    }
}
