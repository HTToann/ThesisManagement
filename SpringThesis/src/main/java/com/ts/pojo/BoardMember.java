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
import java.util.Date;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "board_member")
@NamedQueries({
    @NamedQuery(name = "BoardMember.findAll", query = "SELECT b FROM BoardMember b"),
    @NamedQuery(name = "BoardMember.findByBoardId", query = "SELECT b FROM BoardMember b WHERE b.boardMemberPK.boardId = :boardId"),
    @NamedQuery(name = "BoardMember.findByLecturerId", query = "SELECT b FROM BoardMember b WHERE b.boardMemberPK.lecturerId = :lecturerId"),
    @NamedQuery(name = "BoardMember.findByRoleInBoard", query = "SELECT b FROM BoardMember b WHERE b.roleInBoard = :roleInBoard")})
public class BoardMember implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BoardMemberPK boardMemberPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "role_in_board")
    private String roleInBoard;
    @JoinColumn(name = "board_id", referencedColumnName = "board_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Board board;
    @JoinColumn(name = "lecturer_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;

    @PrePersist
    public void prePersist() {
        if (this.roleInBoard == null)
            this.roleInBoard = "ROLE_MEMBERS";
    }
    public BoardMember() {
    }

    public BoardMember(BoardMemberPK boardMemberPK) {
        this.boardMemberPK = boardMemberPK;
    }

    public BoardMember(BoardMemberPK boardMemberPK, String roleInBoard) {
        this.boardMemberPK = boardMemberPK;
        this.roleInBoard = roleInBoard;
    }

    public BoardMember(int boardId, int lecturerId) {
        this.boardMemberPK = new BoardMemberPK(boardId, lecturerId);
    }

    public BoardMemberPK getBoardMemberPK() {
        return boardMemberPK;
    }

    public void setBoardMemberPK(BoardMemberPK boardMemberPK) {
        this.boardMemberPK = boardMemberPK;
    }

    public String getRoleInBoard() {
        return roleInBoard;
    }

    public void setRoleInBoard(String roleInBoard) {
        this.roleInBoard = roleInBoard;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
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
        hash += (boardMemberPK != null ? boardMemberPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoardMember)) {
            return false;
        }
        BoardMember other = (BoardMember) object;
        if ((this.boardMemberPK == null && other.boardMemberPK != null) || (this.boardMemberPK != null && !this.boardMemberPK.equals(other.boardMemberPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ts.pojo.BoardMember[ boardMemberPK=" + boardMemberPK + " ]";
    }
    
}
