/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Faculty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public interface FacultyService {
    Faculty getById(int id);
    List<Faculty> getAll();
    Faculty create(Map<String, String> payload);
    Faculty update(int id,Map<String, String> payload);
    Faculty getByName(String name);
    List<Faculty> searchByName(String keyword);
    void delete(int id);
}
