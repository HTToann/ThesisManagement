/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.pojo;

import java.util.List;

/**
 *
 * @author Lenovo
 */
public class BoardRequestDTO {

    private List<MemberRequest> members;

    public List<MemberRequest> getMembers() {
        return members;
    }

    public void setMembers(List<MemberRequest> members) {
        this.members = members;
    }

 

    public static class MemberRequest {

        private int lecturerId;
        private String role;

        public int getLecturerId() {
            return lecturerId;
        }

        public void setLecturerId(int lecturerId) {
            this.lecturerId = lecturerId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
