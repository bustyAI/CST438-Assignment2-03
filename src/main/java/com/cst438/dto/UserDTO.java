package com.cst438.dto;
/*
 * Data Transfer Object for user data
 * A user can be a STUDENT, ADMIN, or INSTRUCTOR type
 */
public record UserDTO (
        int id,
        String name,
        String email,
        String type) {
}
