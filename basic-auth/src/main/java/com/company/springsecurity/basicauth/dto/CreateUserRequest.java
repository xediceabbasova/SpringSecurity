package com.company.springsecurity.basicauth.dto;

import com.company.springsecurity.basicauth.model.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record CreateUserRequest(
        String name,
        String username,
        String password,
        Set<Role> authorities
) {
}
