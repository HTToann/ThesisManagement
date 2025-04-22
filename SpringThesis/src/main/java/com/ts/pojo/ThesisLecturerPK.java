/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
@Embeddable
public class ThesisLecturerPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "thesis_id")
    private int thesisId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lecturer_id")
    private int lecturerId;

    public ThesisLecturerPK() {
    }

    public ThesisLecturerPK(int thesisId, int lecturerId) {
        this.thesisId = thesisId;
        this.lecturerId = lecturerId;
    }

    public int getThesisId() {
        return thesisId;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) thesisId;
        hash += (int) lecturerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThesisLecturerPK)) {
            return false;
        }
        ThesisLecturerPK other = (ThesisLecturerPK) object;
        if (this.thesisId != other.thesisId) {
            return false;
        }
        if (this.lecturerId != other.lecturerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.ThesisLecturerPK[ thesisId=" + thesisId + ", lecturerId=" + lecturerId + " ]";
    }
    
}
