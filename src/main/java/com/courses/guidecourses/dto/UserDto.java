package com.courses.guidecourses.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Set;

@Value @Builder
public class UserDto {
    Long    id;
    String  keycloakId;
    String  username;
    String  firstName;
    String  lastName;
    String  email;
    String  phone;
    String  avatarUrl;
    Set<String> roles;
    Instant createdAt;
}