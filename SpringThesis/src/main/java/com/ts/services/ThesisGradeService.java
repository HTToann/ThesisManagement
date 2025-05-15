/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.ThesisGrade;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface ThesisGradeService {
    void add(Map<String, String> payload);
    void update(Map<String, String> payload);
    void delete(Map<String, String> payload);
    List<ThesisGrade> getAll();
    List<ThesisGrade> getByThesisId(int thesisId);
    List<ThesisGrade> getByBoardId(int boardId);
    List<ThesisGrade> getByLecturerAndBoardAndThesis(int lecturerId, int boardId,int thesisId);
}
