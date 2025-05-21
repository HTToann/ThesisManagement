/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ts.services;

import com.ts.pojo.ThesisMemberInfoDTO;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ThesisMemberService {
    List<ThesisMemberInfoDTO> getThesisMembersByThesisId(int thesisId);
}
