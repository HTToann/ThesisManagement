/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Faculty;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface FacultyRepository {
    Faculty getById(int id);
    Faculty getByName(String name);
    List<Faculty> getAllFaculties();
    Faculty addOrUpdate(Faculty f);
    void delete(int id);
}
