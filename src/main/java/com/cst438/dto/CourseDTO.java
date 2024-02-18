package com.cst438.dto;
/*
 * Data Transfer Object for course data
 */
public record CourseDTO(
        String courseId,
        String title,
        int credits
) {
}
