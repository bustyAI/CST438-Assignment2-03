package com.cst438.controller;


import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.SectionRepository;
import com.cst438.domain.TermRepository;
import com.cst438.domain.UserRepository;
import com.cst438.dto.CourseDTO;
import com.cst438.dto.EnrollmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class EnrollmentController {

    @Autowired
    EnrollmentRepository enrollmentRepository;


    // instructor downloads student enrollments for a section, ordered by student name
    // user must be instructor for the section
    @GetMapping("/sections/{sectionNo}/enrollments")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    public List<EnrollmentDTO> getEnrollments( @PathVariable("sectionNo") int sectionNo ) {

        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsBySectionNoOrderByStudentName(sectionNo);
        if (enrollments.size() < 1){
            throw  new ResponseStatusException( HttpStatus.NOT_FOUND, "section not found ( " + sectionNo );
        } else{
            List<EnrollmentDTO> dto_list = new ArrayList<>();
            for (Enrollment e : enrollments) {
                dto_list.add(new EnrollmentDTO(e.getEnrollmentId(), e.getGrade(), e.getUser().getId(),
                e.getUser().getName(), e.getUser().getEmail(), e.getSection().getCourse().getCourseId(),
                e.getSection().getSecId(), e.getSection().getSectionNo(), e.getSection().getBuilding(), 
                e.getSection().getRoom(), e.getSection().getTimes(), e.getSection().getCourse().getCredits(), 
                e.getSection().getTerm().getYear(), e.getSection().getTerm().getSemester()));
            }
            return dto_list;
        }
        
    }

    // instructor uploads enrollments with the final grades for the section
    // user must be instructor for the section
    @PutMapping("/enrollments")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    public void updateEnrollmentGrade(@RequestBody List<EnrollmentDTO> dlist) {
        int counter = 1;
        for (EnrollmentDTO e : dlist){
            if (e==null){
                throw  new ResponseStatusException( HttpStatus.NOT_FOUND, "enrollment not found ("+ counter + " in list)");
            } else {
                Enrollment enrollment = 
                enrollmentRepository.findEnrollmentBySectionNoAndStudentId(e.sectionNo(), e.studentId());
                enrollment.setGrade(e.grade());
                enrollmentRepository.save(enrollment);
            }
            counter++;
        }
    }

}