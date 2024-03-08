package com.cst438.domain;

import jakarta.persistence.*;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="grade_id")
    private int gradeId;

    private Integer assignmentScore;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Grade grade;
    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getAssignmentScore() {
        return assignmentScore;
    }

    public void setAssignmentScore(Integer assignmentScore) {
        this.assignmentScore = assignmentScore;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }
}
