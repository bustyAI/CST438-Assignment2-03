package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.EnrollmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {


   // student gets transcript showing list of all enrollments
   // studentId will be temporary until Login security is implemented
   @GetMapping("/transcript")
   public List<EnrollmentDTO> getTranscript(@RequestParam("studentId") int studentId) {

       // TODO remove the following line when done

       // list course_id, sec_id, title, credit, grade in chronological order
       // user must be a student

       return null;
   }

    // student gets a list of their enrollments for the given year, semester
    // user must be student
   @GetMapping("/enrollment")
   public List<EnrollmentDTO> getSchedule(
           @RequestParam("year") int year,
           @RequestParam("semester") String semester,
           @RequestParam("studentId") int studentId) {


     // TODO remove the following line when done

       return null;
   }


    // student enrolls into a section
    // user must be student
    @PostMapping("/enrollment")
    public EnrollmentDTO addCourse(@RequestBody EnrollmentDTO dto) {

        // TODO remove the following line when done

        // check that the Section entity with primary key sectionNo exists
        // check that today is between addDate and addDeadline for the section
        // check that student is not already enrolled into this section
        // create a new enrollment entity and save.  The enrollment grade will
        // be NULL until instructor enters final grades for the course.

        return null;

    }

    // student drops a course
    // user must be student
   @DeleteMapping("/enrollment/{enrollmentId}")
   public void dropCourse(@PathVariable("enrollmentId") int enrollmentId) {

       // TODO

       //  check that today is before dropDeadline for section
   }
}