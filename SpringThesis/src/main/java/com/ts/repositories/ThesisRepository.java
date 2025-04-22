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
    void deleteThesisById(int thesisId);
    List<Thesis> getAllThesis();
    Thesis addOrUpdate(Thesis thesis);
}
