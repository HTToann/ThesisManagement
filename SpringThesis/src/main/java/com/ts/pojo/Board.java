/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "board")
@NamedQueries({
    @NamedQuery(name = "Board.findAll", query = "SELECT b FROM Board b"),
    @NamedQuery(name = "Board.findByBoardId", query = "SELECT b FROM Board b WHERE b.boardId = :boardId"),
    @NamedQuery(name = "Board.findByCreatedAt", query = "SELECT b FROM Board b WHERE b.createdAt = :createdAt"),
    @NamedQuery(name = "Board.findByIsLocked", query = "SELECT b FROM Board b WHERE b.isLocked = :isLocked")})
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "board_id")
    private Integer boardId;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "is_locked")
    private Boolean isLocked;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board")
    private Set<ThesisGrade> thesisGradeSet;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board")
    private Set<BoardMember> boardMemberSet;
    @JsonIgnore
    @OneToMany(mappedBy = "boardId")
    private Set<Thesis> thesisSet;

    public Board() {
    }
    @PrePersist
    public void prePersist() {
        if (this.isLocked == null)
            this.isLocked = false;
        if (this.createdAt == null)
            this.createdAt = new Date();
    }
    public Board(Integer boardId) {
        this.boardId = boardId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Set<ThesisGrade> getThesisGradeSet() {
        return thesisGradeSet;
    }

    public void setThesisGradeSet(Set<ThesisGrade> thesisGradeSet) {
        this.thesisGradeSet = thesisGradeSet;
    }

    public Set<BoardMember> getBoardMemberSet() {
        return boardMemberSet;
    }

    public void setBoardMemberSet(Set<BoardMember> boardMemberSet) {
        this.boardMemberSet = boardMemberSet;
    }

    public Set<Thesis> getThesisSet() {
        return thesisSet;
    }

    public void setThesisSet(Set<Thesis> thesisSet) {
        this.thesisSet = thesisSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (boardId != null ? boardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Board)) {
            return false;
        }
        Board other = (Board) object;
        if ((this.boardId == null && other.boardId != null) || (this.boardId != null && !this.boardId.equals(other.boardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.Board[ boardId=" + boardId + " ]";
    }
    
}
