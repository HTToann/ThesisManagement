/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Faculty;
import com.ts.repositories.FacultyRepository;
import com.ts.services.FacultyService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class FacultyServiceImpl implements FacultyService {

    @Autowired
    private FacultyRepository repo;

    @Override
    public Faculty getById(int id) {
        return this.repo.getById(id);
    }
    @Override
    public Faculty getByName(String name) {
        return this.repo.getByName(name);
    }
    @Override
    public List<Faculty> getAll() {
        return this.repo.getAllFaculties();
    }

    @Override
    public Faculty create(Map<String, String> payload) {
        Faculty f = new Faculty();
        String name = payload.get("name");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name không được để trống");
        }
        Faculty existing = this.repo.getByName(name);
        if (existing != null) {
            throw new IllegalArgumentException("Khoa này đã tồn tại!!");
        }
        f.setName(name.trim());
        return this.repo.addOrUpdate(f);
    }

    @Override
    public void delete(int id) {
        this.repo.delete(id);
    }

    @Override
    public Faculty update(int id, Map<String, String> payload) {
        Faculty f = this.repo.getById(id);
        String name = payload.get("name");
        if (f == null) {
            throw new IllegalArgumentException("Không tồn tại Faculty!!");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name không được để trống");
        }
        Faculty existing = this.repo.getByName(name);
        if (existing != null) {
            throw new IllegalArgumentException("Faculty này đã tồn tại!!");
        }
        f.setName(name.trim());
        return this.repo.addOrUpdate(f);
    }

    @Override
    public List<Faculty> searchByName(String keyword) {
        return this.repo.searchByName(keyword);
    }

}
