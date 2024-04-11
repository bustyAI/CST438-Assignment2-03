package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.EnrollmentDTO;
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


import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
public class StudentControllerUnitTest {

    @Autowired
    MockMvc mvc;


    // Unit test for enroll that is past deadline
    // Unit test invokes REST api POST /enrollments/sections/{sectionNo}?studentId={id}.
    // The request is unsuccessful because the date is past the add deadline for the section.
    // The test has assert statements that check for bad status code and error message.
    @Test
    public void enrollIntoSectionPastDeadline() throws  Exception {
        // Testing with section that is past enrollment
        int sectionNo = 1;
        int studentId = 3;

        MockHttpServletResponse response;

        response = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/enrollments/sections/" + sectionNo + "?studentId=" + studentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(400, response.getStatus());

        String message = response.getErrorMessage();

        assertEquals("Unable to enroll. Enrollment period has ended for: " + sectionNo, message);
    }

    private static <T> T  fromJsonString(String str, Class<T> valueType ) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
