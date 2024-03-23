package com.cst438.controller;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Grade;
import com.cst438.domain.GradeRepository;
import com.cst438.dto.AssignmentDTO;
import com.cst438.dto.GradeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;


@AutoConfigureMockMvc
@SpringBootTest
public class AssignmentControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Test
    public void addAssignment() throws Exception {

        MockHttpServletResponse response;

        Date testDate = new Date(2024, 10, 07);
        AssignmentDTO assignment = new AssignmentDTO(
                0,
                "addAssignment Test Entry",
                testDate,
                "cst338",
                1,
                6
        );

        // issue a http POST request to SpringTestServer
        // specify MediaType for request and response data
        // convert assignment to String data and set as request content
        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/assignments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(assignment)))
                        .andReturn()
                        .getResponse();

        // check the response code for 200 meaning OK
        assertEquals(200, response.getStatus());

        // return data converted from String to DTO
        AssignmentDTO result = fromJsonString(response.getContentAsString(), AssignmentDTO.class);

        // primary key should have a non zero value from the database
        assertNotEquals(0, result.id());
        assertEquals("cst338", result.courseId());
        assertEquals("addAssignment Test Entry", result.title());
        assertEquals(1, result.secId());
        assertEquals(6, result.secNo());

        // check the database
        Assignment a = assignmentRepository.findById(result.id()).orElse(null);
        assertNotNull(a);
        assertEquals("cst338", a.getSection().getCourse().getCourseId());

        // clean up after test. issue http DELETE request for assignment
        response = mvc.perform(
                        MockMvcRequestBuilders
                                .delete("/assignments/"+result.id()))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // check database for delete
        a = assignmentRepository.findById(result.id()).orElse(null);
        assertNull(a);  // assignment should not be found after delete
    }

    //originally I had implemented this but realized that the courseId is never input manually
    // leaving test in case that ever changes
    // @Test
    // public void addAssignmentFailsBadCourse() throws Exception {

    //     MockHttpServletResponse response;

    //     // course id cst599 does not exist.
    //     LocalDate localDate = LocalDate.of(2024, 10, 07);
    //     Date testDate = Date.valueOf(localDate);
    //     AssignmentDTO assignment = new AssignmentDTO(
    //             0,
    //             "addAssignment Test Entry",
    //             testDate,
    //             "cst599",
    //             1,
    //             6
    //     );

    //     // issue the POST request
    //     response = mvc.perform(
    //                     MockMvcRequestBuilders
    //                             .post("/assignments")
    //                             .accept(MediaType.APPLICATION_JSON)
    //                             .contentType(MediaType.APPLICATION_JSON)
    //                             .content(asJsonString(assignment)))
    //             .andReturn()
    //             .getResponse();

    //     // response should be 404, NOT_FOUND
    //     assertEquals(404, response.getStatus());

    //     // check the expected error message
    //     String message = response.getErrorMessage();
    //     assertEquals("course not found cst599", message);

    // }

    @Test
    public void addAssignmentFailsBadDate( ) throws Exception {

        MockHttpServletResponse response;

        // invalid date
        LocalDate localDate = LocalDate.of(2022, 3, 22);
        Date testDate = Date.valueOf(localDate);
        AssignmentDTO assignment = new AssignmentDTO(
                0,
                "addAssignment Test Entry",
                testDate,
                "cst338",
                1,
                6
        );

        // issue the POST request
        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/assignments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(assignment)))
                .andReturn()
                .getResponse();

        // response should be 400, BAD_REQUEST
        // a bad request 
        assertEquals(400, response.getStatus());

        // check the expected error message
        String message = response.getErrorMessage();
        assertEquals("invalid due date 2022-03-22", message);

    }

    @Test
    public void addAssignmentFailsBadSectionNumber( ) throws Exception {

        MockHttpServletResponse response;

        // invalid section number
        LocalDate localDate = LocalDate.of(2022, 3, 22);
        Date testDate = Date.valueOf(localDate);
        AssignmentDTO assignment = new AssignmentDTO(
                0,
                "addAssignment Test Entry",
                testDate,
                "cst338",
                1,
                9001
        );

        // issue the POST request
        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/assignments")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(assignment)))
                .andReturn()
                .getResponse();

        // response should be 404, NOT_FOUND
        // section not found
        assertEquals(404, response.getStatus());

        // check the expected error message
        String message = response.getErrorMessage();
        assertEquals("section not found: 9001", message);

    }

    @Test
    public void addGradeToAssignment() throws Exception {

        MockHttpServletResponse response;

        // Only assignemnt in DB
        int assignmentId = 1;

        // GET request
        response = mvc.perform(
                        MockMvcRequestBuilders
                        .get("/assignments/" + assignmentId + "/grades")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Response should be OK (200)
        assertEquals(200, response.getStatus());

        // Return data converted from String to DTO
        List<GradeDTO> gradeDTOs = Arrays.asList(fromJsonString(response.getContentAsString(), GradeDTO[].class));
        
        // Update the grades with scores
        for (GradeDTO gradeDTO : gradeDTOs) {
            // Set the score in the GradeDTO object
            gradeDTO = new GradeDTO(
                    gradeDTO.gradeId(),
                    gradeDTO.studentName(),
                    gradeDTO.studentEmail(),
                    gradeDTO.assignmentTitle(),
                    gradeDTO.courseId(),
                    gradeDTO.sectionId(),
                    90 
            );
            assertEquals(90, gradeDTO.score());
        }

        // PUT request to save the updated grades
        response = mvc.perform(
                        MockMvcRequestBuilders
                        .put("/grades")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(gradeDTOs)))
            .andReturn()
            .getResponse();

         // Response should be OK (200)
        assertEquals(200, response.getStatus());
    }

    @Test
    public void invalidAddGradeToAssignment() throws Exception {

        MockHttpServletResponse response;

        // Only assignemnt in DB
        int assignmentId = -1;

        // issue the GET request
        response = mvc.perform(
                        MockMvcRequestBuilders
                        .get("/assignments/" + assignmentId + "/grades")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Response should be 404
        assertEquals(404, response.getStatus());
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
