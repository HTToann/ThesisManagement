/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.pojo;

/**
 *
 * @author Lenovo
 */
public class ThesisMemberInfoDTO {

    /**
     * @return the userId
     */
    private Integer userId;
    private String firstName;
    private String lastName;
    private String role;
    private String lectureRole; // null nếu là student
    private Integer thesisId;
    private boolean isStudent;

    public ThesisMemberInfoDTO(Integer userId, String firstName, String lastName, String role, String lectureRole, Integer thesisId, boolean isStudent) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.lectureRole = lectureRole;
        this.thesisId = thesisId;
        this.isStudent = isStudent;

    }

    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the lectureRole
     */
    public String getLectureRole() {
        return lectureRole;
    }

    /**
     * @param lectureRole the lectureRole to set
     */
    public void setLectureRole(String lectureRole) {
        this.lectureRole = lectureRole;
    }

    /**
     * @return the thesisId
     */
    public Integer getThesisId() {
        return thesisId;
    }

    /**
     * @param thesisId the thesisId to set
     */
    public void setThesisId(Integer thesisId) {
        this.thesisId = thesisId;
    }

    /**
     * @return the isStudent
     */
    public boolean isIsStudent() {
        return isStudent;
    }

    /**
     * @param isStudent the isStudent to set
     */
    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

}
