/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Thesis;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface ThesisService {
    Thesis getThesisById(int thesisId);
    void deleteThesisById(int thesisId);
    List<Thesis> getAllThesis();
    Thesis addThesis(Map<String,String> params);
    Thesis updateThesis(int id,Map<String,String> params);
}
