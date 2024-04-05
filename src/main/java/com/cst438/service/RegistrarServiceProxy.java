package com.cst438.service;

import com.cst438.domain.*;
import com.cst438.dto.*;

import org.hibernate.grammars.hql.HqlParser.SecondContext;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RegistrarServiceProxy {

    Queue registrarServiceQueue = new Queue("registrar_service", true);

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
    TermRepository termRepository;

    public void updateEnrollmentGrade(EnrollmentDTO enrollment) {
        sendMessage("updateEnrollment " + asJsonString(enrollment));
    }

@RabbitListener(queues = "gradebook_service")
public void receiveFromRegistrar(String message) {
    try {
        handleCourseMessage(message);
    } catch (Exception e) {
        System.out.println("Exception in receiveFromRegistrar for course: " + e.getMessage());
    }

    try {
        handleSectionMessage(message);
    } catch (Exception e) {
        System.out.println("Exception in receiveFromRegistrar for section: " + e.getMessage());
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
        Section s = sectionRepository.findById(dto.secId()).orElse(null);
        if (s == null) {
            System.out.println("Error receiveFromRegistrar Section not found " + dto.secId());
        } else {
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
        s.setTerm(term);
        s.setSecId(dto.secId());
        s.setBuilding(dto.building());
        s.setRoom(dto.room());
        s.setTimes(dto.times());
        s.setInstructor_email(dto.instructorEmail());
        sectionRepository.save(s);
    } else if (parts[0].equals("deleteSection")) {
        sectionRepository.deleteById(parts[1]);
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