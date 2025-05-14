package com.courses.guidecourses.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CreateCourseDto(
    String title,
    String description,
    Set<Long> directionIds,
    Set<Long> topicIds,
    String previewUrl,
    BigDecimal price
) {}