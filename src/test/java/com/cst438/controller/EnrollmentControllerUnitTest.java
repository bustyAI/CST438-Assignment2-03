package com.cst438.controller;

import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.dto.EnrollmentDTO;
import com.cst438.domain.Section;
import com.cst438.domain.SectionRepository;
import com.cst438.dto.GradeDTO;
import com.cst438.dto.SectionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc
@SpringBootTest
public class EnrollmentControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    /* Unit test invokes REST api POST /enrollments/sections/{sectionNo}?studentId={id}.
    The request is successful and the test asserts that the returned status code is 200 
    (ok) and that the returned EnrollmentDTO data has expected data. */
    @Test
    public void enrollIntoSection() throws Exception {

        int sectionNo = 9;
        int studentId = 3;
        
        MockHttpServletResponse response;
        response = mvc.perform(
            MockMvcRequestBuilders
                    .post("/enrollments/sections/" + sectionNo + "?studentId=" + studentId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

        // check the response code for 200 meaning OK
        assertEquals(200, response.getStatus());

        // return data converted from String to EnrollmentDTO
        EnrollmentDTO enrollment = fromJsonString(response.getContentAsString(), EnrollmentDTO.class);

        // Assert that returned EnrollmentDTO has proper data
        assertEquals(10000, enrollment.enrollmentId());
        assertEquals(3, enrollment.studentId());
        assertEquals("thomas edison", enrollment.name());
        assertEquals("tedison@csumb.edu", enrollment.email());
        assertEquals("cst363", enrollment.courseId());
        assertEquals(2, enrollment.sectionId());
        assertEquals(9, enrollment.sectionNo());
        assertEquals("052", enrollment.building());
        assertEquals("102", enrollment.room());
        assertEquals("M W 2:00-3:50", enrollment.times());
        assertEquals(4, enrollment.credits());
        assertEquals(2024, enrollment.year());
        assertEquals("Spring", enrollment.semester());
    }
    
    /*  Unit test invokes REST api POST /enrollments/sections/{sectionNo}?studentId={id}. 
    The request is unsuccessful because the student is already enrolled. 
    There are assert statements on the returned status code and error message.*/
    @Test
    public void enrollIntoSectionFailDuplicate() throws Exception {

        int sectionNo = 8;
        int studentId = 3;
        
        MockHttpServletResponse response;
        response = mvc.perform(
            MockMvcRequestBuilders
                    .post("/enrollments/sections/" + sectionNo + "?studentId=" + studentId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

        // Response should be 400
        assertEquals(400, response.getStatus());

        // check the expected error message
        String message = response.getErrorMessage();
        assertEquals("Student is already enrolled in: 8", message);
    }

    @Test
    public void enrollIntoSectionBadSection() throws Exception {

        int sectionNo = 77;
        int studentId = 3;
        
        MockHttpServletResponse response;
        response = mvc.perform(
            MockMvcRequestBuilders
                    .post("/enrollments/sections/" + sectionNo + "?studentId=" + studentId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse();

        // Response should be 404
        assertEquals(404, response.getStatus());

        // check the expected error message
        String message = response.getErrorMessage();
        assertEquals("Section not found matching: 77", message);
    }

//    Unit test invokes REST api GET for the url /sections/{sectionNo}/enrollments.
//    Update the returned list of EnrollmentDTO objects with grades and the invokes
//    PUT /enrollments with a body containing the updates EnrollmentDTO objects.
//    The request is successful and the test contains asserts for the status code.
    @Test
    public void updateEnrollmentGrade() throws Exception {

//        TODO check for duplicated enrollments

        // Updating grade for section 1
        int sectionNo = 1;
        MockHttpServletResponse response;

        // GET request
        response = mvc.perform(MockMvcRequestBuilders
                .get("/sections/" + sectionNo + "/enrollments")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Response should yield OK (200)
        assertEquals(200, response.getStatus());

        // Converting data from string to EnrollmentDTO
        List<EnrollmentDTO> enrollmentDTOs = Arrays
                .asList(fromJsonString(response.getContentAsString(), EnrollmentDTO[].class));

        // Update the enrollment grades
        List<EnrollmentDTO> updatedEnrollmentDTOs = enrollmentDTOs.stream().map(enrollmentDTO -> new EnrollmentDTO(
                enrollmentDTO.enrollmentId(),
                "B",
                enrollmentDTO.studentId(),
                enrollmentDTO.name(),
                enrollmentDTO.email(),
                enrollmentDTO.courseId(),
                enrollmentDTO.sectionId(),
                enrollmentDTO.sectionNo(),
                enrollmentDTO.building(),
                enrollmentDTO.room(),
                enrollmentDTO.times(),
                enrollmentDTO.credits(),
                enrollmentDTO.year(),
                enrollmentDTO.semester()

        )).toList();

        // ensuring all enrollment grades are updated
        for (EnrollmentDTO enrollmentDTO: updatedEnrollmentDTOs) {
            assertEquals("B", enrollmentDTO.grade());
        }

        // PUT request to update grade
        response = mvc.perform(
                MockMvcRequestBuilders
                        .put("/enrollments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedEnrollmentDTOs)))
                .andReturn()
                .getResponse();

        System.out.println(updatedEnrollmentDTOs);
        // Response should yield OK (200)
        assertEquals(200, response.getStatus());

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T  fromJsonString(String str, Class<T> valueType ) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
