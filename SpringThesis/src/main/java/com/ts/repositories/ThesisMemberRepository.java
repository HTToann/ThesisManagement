    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.repositories;

import com.ts.pojo.ThesisMemberInfoDTO;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ThesisMemberRepository {
    List<ThesisMemberInfoDTO> getThesisMembersByThesisId(int thesisId);
}
