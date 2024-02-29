package com.cst438.domain;

import jakarta.persistence.*;

@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="grade_id")
    private int gradeId;
 
    // TODO complete this class
    // add additional attribute for score
    // add relationship between grade and assignment entities
    // add relationship between grade and enrollment entities
    // add getter/setter methods
}
