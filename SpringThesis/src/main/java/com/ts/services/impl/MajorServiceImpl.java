/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.Faculty;
import com.ts.pojo.Major;
import com.ts.repositories.FacultyRepository;
import com.ts.repositories.MajorRepository;
import com.ts.services.MajorService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class MajorServiceImpl implements MajorService {

    @Autowired
    private MajorRepository majorRepo;
    @Autowired
    private FacultyRepository facultyRepo;

    @Override
    public Major getById(int id) {
        return majorRepo.getById(id);
    }

    @Override
    public Major getByName(String name) {
        return majorRepo.getByName(name);
    }

    @Override
    public List<Major> getAllMajors() {
        return majorRepo.getAllMajors();
    }

    @Override
    public void delete(int id) {
        majorRepo.delete(id);
    }

    @Override
    public Major addMajor(Map<String, String> payload) {
        String name = payload.get("name");
        String facultyIdStr = payload.get("facultyId");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name không được để trống");
        }
        if (facultyIdStr == null || facultyIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("FacultyId không được để trống");
        }
        Major m = new Major();
        int facultyId = Integer.parseInt(facultyIdStr.trim());
        Faculty f = this.facultyRepo.getById(facultyId);
        if ( f == null) {
            throw new IllegalArgumentException("Faculty không tồn tại");
        }
        m.setName(name.trim());
        m.setFacultyId(f);
        this.majorRepo.addOrUpdate(m);
        return m;
    }

    @Override
    public Major updateMajor(int id, Map<String, String> payload) {
        Major majorClone= this.majorRepo.getById(id);
        if ( majorClone == null) {
             throw new IllegalArgumentException("Major không tồn tại");
        }
        String name = payload.get("name");
        String facultyIdStr = payload.get("facultyId");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name không được để trống");
        }
        if (facultyIdStr == null || facultyIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("FacultyId không được để trống");
        }
        Major m = new Major();
        int facultyId = Integer.parseInt(facultyIdStr.trim());
        Faculty f = this.facultyRepo.getById(facultyId);
        if ( f == null) {
            throw new IllegalArgumentException("Faculty không tồn tại");
        }
        m.setName(name.trim());
        m.setFacultyId(f);
        this.majorRepo.addOrUpdate(m);
        return m;
    }

    @Override
    public List<Major> searchByName(String keyword) {
        return this.majorRepo.searchByName(keyword);
    }
}
