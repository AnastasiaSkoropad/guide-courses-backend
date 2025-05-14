package com.courses.guidecourses.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record CourseDto(
    Long id,
    String title,
    String description,
    String previewUrl,
    BigDecimal price,
    Set<Long> directionIds,
    Set<Long> topicIds,
    Long likeCount,
    Long dislikeCount,
    Long commentCount,
    Instant createdAt
) {}