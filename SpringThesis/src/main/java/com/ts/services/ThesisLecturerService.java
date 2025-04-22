/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.ThesisLecturer;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface ThesisLecturerService {
    void add(Map<String,String> payload);
    List<ThesisLecturer> getByThesisId(int thesisId);
    void delete(Map<String, String> payload) ;
    void updateLecturer(Map<String, String> payload);
}
