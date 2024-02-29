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
 
    // TODO  complete this class
    // add additional attributes for title, dueDate
    // add relationship between assignment and section entities
    // add getter and setter methods
}
