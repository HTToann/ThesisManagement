/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ts.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "thesis_grade")
@NamedQueries({
    @NamedQuery(name = "ThesisGrade.findAll", query = "SELECT t FROM ThesisGrade t"),
    @NamedQuery(name = "ThesisGrade.findByBoardId", query = "SELECT t FROM ThesisGrade t WHERE t.thesisGradePK.boardId = :boardId"),
    @NamedQuery(name = "ThesisGrade.findByThesisId", query = "SELECT t FROM ThesisGrade t WHERE t.thesisGradePK.thesisId = :thesisId"),
    @NamedQuery(name = "ThesisGrade.findByLecturerId", query = "SELECT t FROM ThesisGrade t WHERE t.thesisGradePK.lecturerId = :lecturerId"),
    @NamedQuery(name = "ThesisGrade.findByCriteriaId", query = "SELECT t FROM ThesisGrade t WHERE t.thesisGradePK.criteriaId = :criteriaId"),
    @NamedQuery(name = "ThesisGrade.findByScore", query = "SELECT t FROM ThesisGrade t WHERE t.score = :score")})
public class ThesisGrade implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ThesisGradePK thesisGradePK;
    @Column(name = "score")
    private Double score;
    
    @JsonIgnore
    @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Board board;
    
    @JsonIgnore
    @JoinColumn(name = "criteria_id", referencedColumnName = "criteria_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Criteria criteria;
    
    @JsonIgnore
    @JoinColumn(name = "thesis_id", referencedColumnName = "thesis_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Thesis thesis;
    
    @JoinColumn(name = "lecturer_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @JsonIgnore

    private Users users;

    public ThesisGrade() {
    }

    public ThesisGrade(ThesisGradePK thesisGradePK) {
        this.thesisGradePK = thesisGradePK;
    }

    public ThesisGrade(int boardId, int thesisId, int lecturerId, int criteriaId) {
        this.thesisGradePK = new ThesisGradePK(boardId, thesisId, lecturerId, criteriaId);
    }

    public ThesisGradePK getThesisGradePK() {
        return thesisGradePK;
    }

    public void setThesisGradePK(ThesisGradePK thesisGradePK) {
        this.thesisGradePK = thesisGradePK;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
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
        hash += (thesisGradePK != null ? thesisGradePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThesisGrade)) {
            return false;
        }
        ThesisGrade other = (ThesisGrade) object;
        if ((this.thesisGradePK == null && other.thesisGradePK != null) || (this.thesisGradePK != null && !this.thesisGradePK.equals(other.thesisGradePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.ThesisGrade[ thesisGradePK=" + thesisGradePK + " ]";
    }

}
