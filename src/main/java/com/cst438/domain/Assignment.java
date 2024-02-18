package com.cst438.domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="assignment_id")
    private int assignmentId;
    private String title;
    @Column(name="due_date")
    private Date dueDate;
    @ManyToOne
    @JoinColumn(name="section_no", nullable=false)
    private Section section;

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getDueDateAsString() {
        if (this.dueDate!=null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            return f.format(this.dueDate);
        } else {
            return null;
        }
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setDueDate(String dueDate) {
        if (dueDate!=null)
            this.dueDate = Date.valueOf(dueDate);
        else
            this.dueDate=null;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
