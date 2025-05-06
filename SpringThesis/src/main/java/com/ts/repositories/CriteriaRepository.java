/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Criteria;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface CriteriaRepository {
    List<Criteria> getAll();
    Criteria getById(int id);
    Criteria getByName(String name);
    List<Criteria> searchByName(String keyword);
    Criteria add(Criteria c);
    Criteria update(Criteria c);
    void delete(int id);
}
