/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Thesis;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface ThesisService {
    Thesis getThesisById(int thesisId);
    List<Thesis> getThesisByName(String kw);
    void deleteThesisById(int thesisId);
    List<Thesis> getAllThesis();
    Thesis addThesis(Map<String,String> payload,Principal principal);
    Thesis updateThesis(int id,Map<String,String> payload);
    Thesis updateBrowsingThesis(int id,Map<String,String> payload);
}
