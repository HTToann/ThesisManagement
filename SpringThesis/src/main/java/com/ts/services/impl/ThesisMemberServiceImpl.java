/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.services.impl;

import com.ts.pojo.ThesisMemberInfoDTO;
import com.ts.repositories.ThesisMemberRepository;
import com.ts.services.ThesisMemberService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class ThesisMemberServiceImpl implements ThesisMemberService{

    @Autowired
    private ThesisMemberRepository repo;
    @Override
    public List<ThesisMemberInfoDTO> getThesisMembersByThesisId(int thesisId) {
        return this.repo.getThesisMembersByThesisId(thesisId);
    }
    
}
