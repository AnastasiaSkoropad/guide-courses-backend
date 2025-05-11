package com.courses.guidecourses.dto;

import jakarta.validation.constraints.NotNull;

public record VoteDto(
        @NotNull
        VoteType type
) {}
