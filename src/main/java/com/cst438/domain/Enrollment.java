package com.cst438.domain;

import jakarta.persistence.*;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="enrollment_id")
    int enrollmentId;
	
	// TODO complete this class
    // add additional attribute for grade
    // create relationship between enrollment and user entities
    // create relationship between enrollment and section entities
    // add getter/setter methods
}
