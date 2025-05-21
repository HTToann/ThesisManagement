/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Thesis;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ThesisRepository {
    Thesis getThesisById(int thesisId);
    List<Thesis> getThesisByName(String kw);
    void deleteThesisById(int thesisId);
    List<Thesis> getAllThesis();
    List<Thesis> getThesesByBoardId(int boardId);
    List<Thesis> getThesesByUserId(int userId);
    Thesis addOrUpdate(Thesis thesis);
}
