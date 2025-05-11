package com.courses.guidecourses.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class AuthResponseDto {
    TokenResponse token;
    UserDto       user;
}