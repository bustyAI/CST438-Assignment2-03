package com.cst438.service;

import com.cst438.domain.*;
import com.cst438.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.grammars.hql.HqlParser.SecondContext;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RegistrarServiceProxy {

    Queue registrarServiceQueue = new Queue("registrar_service", true);

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    @Bean
    public Queue createQueue() {
        return new Queue("gradebook_service", true);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TermRepository termRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    GradeRepository gradeRepository;


    public void updateEnrollmentGrade(EnrollmentDTO enrollment) {
        sendMessage("updateEnrollment " + asJsonString(enrollment));
    }

    public void addAssignment(AssignmentDTO assignment){
        sendMessage("addAssignment" + asJsonString(assignment));
    }

    public void updateAssignment(AssignmentDTO assignment){
        sendMessage("updateAssignment" + asJsonString(assignment));
    }

    public void deleteAssignment(int assignmentId){
        sendMessage("deleteAssignment" + assignmentId);
    }

@RabbitListener(queues = "gradebook_service")
public void receiveFromRegistrar(String message) {
    String[] parts = message.split(" ", 2);
    String functionName = parts[0].toLowerCase();
    
    if (functionName.contains("course")) {
        try {
            handleCourseMessage(message);
        } catch (Exception e) {
            System.out.println("Exception in receiveFromRegistrar for course: " + e.getMessage());
        }
    } else if (functionName.contains("section")) {
        try {
            handleSectionMessage(message);
        } catch (Exception e) {
            System.out.println("Exception in receiveFromRegistrar for section: " + e.getMessage());
        }
    } else if (functionName.contains("user")) {
        try {
            handleUserMessage(message);
        } catch (Exception e) {
            System.out.println("Exception in receiveFromRegistrar for user: " + e.getMessage());
        }
    } else if (functionName.contains("enrollment")){
        try {
            handleEnrollmentMessage(message);
        } catch (Exception e) {
            System.out.println("Exception in receiveFromRegistrar for enrollment: " + e.getMessage());
        }
    }
}

private void handleCourseMessage(String message) {
    System.out.println("receive from Registrar for course: " + message);
    String[] parts = message.split(" ", 2);

    if (parts[0].equals("updateCourse")) {
        CourseDTO dto = fromJsonString(parts[1], CourseDTO.class);
        Course c = courseRepository.findById(dto.courseId()).orElse(null);
        if (c == null) {
            System.out.println("Error receiveFromRegistrar Course not found " + dto.courseId());
        } else {
            c.setTitle(dto.title());
            c.setCredits(dto.credits());
            courseRepository.save(c);
        }
    } else if (parts[0].equals("addCourse")) {
        CourseDTO dto = fromJsonString(parts[1], CourseDTO.class);
        Course c = new Course();
        c.setCourseId(dto.courseId());
        c.setTitle(dto.title());
        c.setCredits(dto.credits());
        courseRepository.save(c);
    } else if (parts[0].equals("deleteCourse")) {
        courseRepository.deleteById(parts[1]);
    } 
}

private void handleSectionMessage(String message) {
    System.out.println("receive from Registrar for section: " + message);
    String[] parts = message.split(" ", 2);

    if (parts[0].equals("updateSection")) {
        SectionDTO dto = fromJsonString(parts[1], SectionDTO.class);
        Section s = sectionRepository.findById(dto.secNo()).orElse(null);
        if (s == null) {
            System.out.println("Error receiveFromRegistrar Section not found " + dto.secNo());
        } else {
            s.setSectionNo(dto.secNo());
            s.setSecId(dto.secId());
            s.setBuilding(dto.building());
            s.setRoom(dto.room());
            s.setTimes(dto.times());
            s.setInstructor_email(dto.instructorEmail());
            sectionRepository.save(s);
        }
    } else if (parts[0].equals("addSection")) {
        SectionDTO dto = fromJsonString(parts[1], SectionDTO.class);
        Term term = termRepository.findByYearAndSemester(dto.year(), dto.semester());
        Section s = new Section();
        Course c = courseRepository.findById(dto.courseId()).orElse(null);

        s.setSectionNo(dto.secNo());
        s.setCourse(c);
        s.setTerm(term);
        s.setSecId(dto.secId());
        s.setBuilding(dto.building());
        s.setRoom(dto.room());
        s.setTimes(dto.times());
        s.setInstructor_email(dto.instructorEmail());
        sectionRepository.save(s);
    } else if  (parts[0].equals("deleteSection")) {
        int sectionNoToDelete = Integer.parseInt(parts[1].trim());
        sectionRepository.deleteById(sectionNoToDelete);
    }
}

private void handleUserMessage(String message) {
    System.out.println("receive from Registrar for user: " + message);
    String[] parts = message.split(" ", 2);

    if (parts[0].equals("updateUser")) {
        UserDTO dto = fromJsonString(parts[1], UserDTO.class);
        User u = userRepository.findById(dto.id()).orElse(null);
        if (u == null) {
            System.out.println("Error receiveFromRegistrar User not found " + dto.id());
        } else {
            u.setName(dto.name());
            u.setEmail(dto.email());
            u.setType(dto.type());
            userRepository.save(u);
        }
    } else if (parts[0].equals("addUser")) {
        UserDTO dto = fromJsonString(parts[1], UserDTO.class);
        User u = new User();
        String password = dto.name()+"2024";
        String enc_password = encoder.encode(password);
        u.setId(dto.id());
        u.setName(dto.name());
        u.setEmail(dto.email());
        u.setType(dto.type());
        u.setPassword(enc_password);
        userRepository.save(u);

    } else if (parts[0].equals("deleteUser")){
        int userToDelete = Integer.parseInt(parts[1].trim());
        userRepository.deleteById(userToDelete);
    } 
}

private void handleEnrollmentMessage(String message) {
    System.out.println("receive from Registrar for enrollment: " + message);
    String[] parts = message.split(" ", 2);

    if (parts[0].equals("addEnrollment")) {
        EnrollmentDTO eDTO = fromJsonString(parts[1], EnrollmentDTO.class);
        Enrollment e = new Enrollment();
        User u = userRepository.findByEmail(eDTO.email());
        Section s = sectionRepository.findById(eDTO.sectionId()).orElse(null);
        e.setEnrollmentId(eDTO.enrollmentId());
        e.setGrade(eDTO.grade());
        e.setUser(u);
        e.setSection(s);
        enrollmentRepository.save(e);

    } 
    // BROKEN NOT SURE WHAT IS CAUSING IT -nathan
    else if (parts[0].equals("dropEnrollment")) {
        int enrollmentId = Integer.parseInt(parts[1].trim());
        System.out.println("enrollmentId: " + enrollmentId);

        Enrollment e = enrollmentRepository.findById(enrollmentId).orElse(null);
        System.out.println("getEnrollmentId: " + e.getEnrollmentId());
        Iterable<Grade> gradeRepo = gradeRepository.findAll();
        List<Grade> grades = new ArrayList<>();
        
        for (Grade grade : gradeRepo){
            if (grade.getEnrollment().getEnrollmentId() == e.getEnrollmentId()){
                grades.add(grade);
            }
        }

        if (e != null){
            for (Grade grade : grades){
                int tempId = grade.getGradeId();
                Grade tempGrade = gradeRepository.findById(tempId).orElse(null);
                if (tempGrade != null){
                    gradeRepository.delete(tempGrade);
                }
            }
        }
        enrollmentRepository.delete(e);
        
    } 
}

    private void sendMessage(String s) {
        System.out.println("Gradebook to Registar " + s);
        rabbitTemplate.convertAndSend(registrarServiceQueue.getName(), s);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJsonString(String str, Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}