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
public class ThesisGradePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "board_id")
    private int boardId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thesis_id")
    private int thesisId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lecturer_id")
    private int lecturerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "criteria_id")
    private int criteriaId;

    public ThesisGradePK() {
    }

    public ThesisGradePK(int boardId, int thesisId, int lecturerId, int criteriaId) {
        this.boardId = boardId;
        this.thesisId = thesisId;
        this.lecturerId = lecturerId;
        this.criteriaId = criteriaId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
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

    public int getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(int criteriaId) {
        this.criteriaId = criteriaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) boardId;
        hash += (int) thesisId;
        hash += (int) lecturerId;
        hash += (int) criteriaId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThesisGradePK)) {
            return false;
        }
        ThesisGradePK other = (ThesisGradePK) object;
        if (this.boardId != other.boardId) {
            return false;
        }
        if (this.thesisId != other.thesisId) {
            return false;
        }
        if (this.lecturerId != other.lecturerId) {
            return false;
        }
        if (this.criteriaId != other.criteriaId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.ThesisGradePK[ boardId=" + boardId + ", thesisId=" + thesisId + ", lecturerId=" + lecturerId + ", criteriaId=" + criteriaId + " ]";
    }
    
}
