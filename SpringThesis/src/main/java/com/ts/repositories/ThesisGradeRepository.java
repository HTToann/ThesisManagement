/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.ThesisGrade;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ThesisGradeRepository {
    void add(ThesisGrade tg);
    void update(ThesisGrade tg);
    void delete(int boardId, int thesisId, int lecturerId, int criteriaId);
    List<ThesisGrade> getAll();
    List<ThesisGrade> getByThesisId(int thesisId);
    List<ThesisGrade> getByBoardId(int boardId);
    List<ThesisGrade> getByLecturerAndBoardAndThesis(int lecturerId, int boardId,int thesisId);
    ThesisGrade getByCompositeKey(int boardId, int thesisId, int lecturerId, int criteriaId);
}
