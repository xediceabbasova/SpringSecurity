package com.company.springsecurity.jwttoken.dto;

public record AuthRequest(
        String username,
        String password
) {
}
