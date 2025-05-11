package com.courses.guidecourses.dto;

import java.util.Set;

public record UpdateCourseDto(
    String title,
    String description,
    Set<Long> directionIds,
    Set<Long> topicIds
) {}