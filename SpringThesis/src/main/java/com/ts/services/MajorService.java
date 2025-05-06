/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.Major;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Lenovo
 */
public interface MajorService {
    Major getById(int id);
    Major getByName(String name);
    List<Major> searchByName(String keyword);
    List<Major> getAllMajors();
    Major addMajor(@RequestBody Map<String,String> payload);
    Major updateMajor(int id,@RequestBody Map<String,String> payload);
    void delete(int id);
}
