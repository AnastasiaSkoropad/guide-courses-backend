package com.courses.guidecourses.dto;

import java.time.Instant;

public record CommentDto(
        Long id,
        String text,
        Instant createdAt,
        UserDto user
) {}