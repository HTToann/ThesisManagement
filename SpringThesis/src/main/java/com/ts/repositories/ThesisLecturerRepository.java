/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.ThesisLecturer;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ThesisLecturerRepository {
    void add(ThesisLecturer tl);
    List<ThesisLecturer> getByThesisId(int thesisId);
    List<ThesisLecturer> getByThesisName(String kw);
    List<ThesisLecturer> getAll();
    void delete(int thesisId, int lecturerId);
    void update(int thesisId, int oldLecturerId, int newLecturerId);
    void updateRole(ThesisLecturer ts);
    ThesisLecturer getByCompositeKey(int thesisId, int lecturerId);
}
