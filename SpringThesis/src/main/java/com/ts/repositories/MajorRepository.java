/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.Major;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface MajorRepository {
    Major getById(int id);
    Major getByName(String name);
    List<Major> getAllMajors();
    List<Major> searchByName(String keyword);
    Major addOrUpdate(Major major);
    void delete(int id);
}
