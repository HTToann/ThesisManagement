/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "thesis_lecturer")
@NamedQueries({
    @NamedQuery(name = "ThesisLecturer.findAll", query = "SELECT t FROM ThesisLecturer t"),
    @NamedQuery(name = "ThesisLecturer.findByThesisId", query = "SELECT t FROM ThesisLecturer t WHERE t.thesisLecturerPK.thesisId = :thesisId"),
    @NamedQuery(name = "ThesisLecturer.findByLecturerId", query = "SELECT t FROM ThesisLecturer t WHERE t.thesisLecturerPK.lecturerId = :lecturerId"),
    @NamedQuery(name = "ThesisLecturer.findByLectureRole", query = "SELECT t FROM ThesisLecturer t WHERE t.lectureRole = :lectureRole")})
public class ThesisLecturer implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ThesisLecturerPK thesisLecturerPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "lecture_role")
    private String lectureRole;
    @JoinColumn(name = "thesis_id", referencedColumnName = "thesis_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Thesis thesis;
    @JoinColumn(name = "lecturer_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;

    @PrePersist
    public void prePersist() {
        if (this.lectureRole == null)
            this.lectureRole = "ROLE_MAIN_ADVISOR";
    }
    public ThesisLecturer() {
    }

    public ThesisLecturer(ThesisLecturerPK thesisLecturerPK) {
        this.thesisLecturerPK = thesisLecturerPK;
    }

    public ThesisLecturer(ThesisLecturerPK thesisLecturerPK, String lectureRole) {
        this.thesisLecturerPK = thesisLecturerPK;
        this.lectureRole = lectureRole;
    }

    public ThesisLecturer(int thesisId, int lecturerId) {
        this.thesisLecturerPK = new ThesisLecturerPK(thesisId, lecturerId);
    }

    public ThesisLecturerPK getThesisLecturerPK() {
        return thesisLecturerPK;
    }

    public void setThesisLecturerPK(ThesisLecturerPK thesisLecturerPK) {
        this.thesisLecturerPK = thesisLecturerPK;
    }

    public String getLectureRole() {
        return lectureRole;
    }

    public void setLectureRole(String lectureRole) {
        this.lectureRole = lectureRole;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (thesisLecturerPK != null ? thesisLecturerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThesisLecturer)) {
            return false;
        }
        ThesisLecturer other = (ThesisLecturer) object;
        if ((this.thesisLecturerPK == null && other.thesisLecturerPK != null) || (this.thesisLecturerPK != null && !this.thesisLecturerPK.equals(other.thesisLecturerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.ThesisLecturer[ thesisLecturerPK=" + thesisLecturerPK + " ]";
    }
    
}
