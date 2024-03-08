package com.cst438.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="enrollment_id")
    int enrollmentId;

    private String finalGrade;

    @OneToMany(mappedBy = "enrollment")
    List<Grade> finalGrades;

    @ManyToOne
    @JoinColumn(name = "section_no", nullable = false)
    private Section section;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User student;

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public List<Grade> getFinalGrades() {
        return finalGrades;
    }

    public void setFinalGrades(List<Grade> finalGrades) {
        this.finalGrades = finalGrades;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
