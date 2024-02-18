package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnrollmentRepository extends CrudRepository<Enrollment, Integer> {
    @Query("select e from Enrollment e where e.section.sectionNo=:sectionNo order by e.student.name")
    List<Enrollment> findEnrollmentsBySectionNoOrderByStudentName(int sectionNo);

    @Query("select e from Enrollment e " +
            "where e.section.course.courseId=:courseId and " +
            " e.section.secId=:secId and e.section.term.year=:year and " +
            " e.section.term.semester=:semester " +
            " order by e.student.name")
    List<Enrollment> findByCourseIdAndSecIdAndYearAndSemesterOrderByStudentName(String courseId, int secId, int year, String semester);

    @Query("select e from Enrollment e where e.student.id=:studentId order by e.section.term.termId")
    List<Enrollment> findEnrollmentsByStudentIdOrderByTermId(int studentId);

    @Query("select e from Enrollment e where e.section.term.year=:year and e.section.term.semester=:semester and e.student.id=:studentId order by e.section.course.courseId")
    List<Enrollment> findByYearAndSemesterOrderByCourseId(int year, String semester, int studentId);

    @Query("select e from Enrollment e where e.section.sectionNo=:sectionNo and e.student.id=:studentId")
    Enrollment findEnrollmentBySectionNoAndStudentId(int sectionNo, int studentId);
}
