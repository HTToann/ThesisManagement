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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "thesis")
@NamedQueries({
    @NamedQuery(name = "Thesis.findAll", query = "SELECT t FROM Thesis t"),
    @NamedQuery(name = "Thesis.findByThesisId", query = "SELECT t FROM Thesis t WHERE t.thesisId = :thesisId"),
    @NamedQuery(name = "Thesis.findByTitle", query = "SELECT t FROM Thesis t WHERE t.title = :title"),
    @NamedQuery(name = "Thesis.findByDescription", query = "SELECT t FROM Thesis t WHERE t.description = :description"),
    @NamedQuery(name = "Thesis.findByYear", query = "SELECT t FROM Thesis t WHERE t.year = :year"),
    @NamedQuery(name = "Thesis.findByCreatedAt", query = "SELECT t FROM Thesis t WHERE t.createdAt = :createdAt"),
    @NamedQuery(name = "Thesis.findByIsLocked", query = "SELECT t FROM Thesis t WHERE t.isLocked = :isLocked")})
public class Thesis implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "year")
    private int year;
    @Size(max = 50)
    @Column(name = "semester")
    private String semester;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    private Double score;


    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "thesis_id")
    private Integer thesisId;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "is_locked")
    private Boolean isLocked;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "thesis")
    private Set<ThesisGrade> thesisGradeSet;
    @JsonIgnore
    @OneToMany(mappedBy = "thesisId")
    private Set<Student> studentSet;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "thesis")
    private Set<ThesisLecturer> thesisLecturerSet;
    @JoinColumn(name = "board_id", referencedColumnName = "board_id")
    @ManyToOne
    @JsonIgnore
    private Board boardId;

    @PrePersist
    public void prePersist() {
        if (this.isLocked == null)
            this.isLocked = false;
        if (this.createdAt == null)
            this.createdAt = new Date();
        if(this.year==0)
            this.year = LocalDate.now().getYear();  // Trả về 2025
    }
    public Thesis() {
    }

    public Thesis(Integer thesisId) {
        this.thesisId = thesisId;
    }

    public Thesis(Integer thesisId, String title, int year) {
        this.thesisId = thesisId;
        this.title = title;
        this.year = year;
    }

    public Integer getThesisId() {
        return thesisId;
    }

    public void setThesisId(Integer thesisId) {
        this.thesisId = thesisId;
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

    public Set<Student> getStudentSet() {
        return studentSet;
    }

    public void setStudentSet(Set<Student> studentSet) {
        this.studentSet = studentSet;
    }

    public Set<ThesisLecturer> getThesisLecturerSet() {
        return thesisLecturerSet;
    }

    public void setThesisLecturerSet(Set<ThesisLecturer> thesisLecturerSet) {
        this.thesisLecturerSet = thesisLecturerSet;
    }

    public Board getBoardId() {
        return boardId;
    }

    public void setBoardId(Board boardId) {
        this.boardId = boardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (thesisId != null ? thesisId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thesis)) {
            return false;
        }
        Thesis other = (Thesis) object;
        if ((this.thesisId == null && other.thesisId != null) || (this.thesisId != null && !this.thesisId.equals(other.thesisId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.Thesis[ thesisId=" + thesisId + " ]";
    }

    /**
     * @return the semester
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param semester the semester to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
    
}
