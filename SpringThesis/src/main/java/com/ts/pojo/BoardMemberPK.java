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
public class BoardMemberPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "board_id")
    private int boardId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lecturer_id")
    private int lecturerId;

    public BoardMemberPK() {
    }

    public BoardMemberPK(int boardId, int lecturerId) {
        this.boardId = boardId;
        this.lecturerId = lecturerId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
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
        hash += (int) boardId;
        hash += (int) lecturerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoardMemberPK)) {
            return false;
        }
        BoardMemberPK other = (BoardMemberPK) object;
        if (this.boardId != other.boardId) {
            return false;
        }
        if (this.lecturerId != other.lecturerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.BoardMemberPK[ boardId=" + boardId + ", lecturerId=" + lecturerId + " ]";
    }
    
}
