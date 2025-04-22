/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Criteria;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface CriteriaService {
    List<Criteria> getAll();
    Criteria getById(int id);
    Criteria add(Map<String, String> payload);
    Criteria update(int id, Map<String, String> payload);
    void delete(int id);
}
