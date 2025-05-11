package com.courses.guidecourses.dto;

import java.util.Set;

public record CourseDto(
    Long id,
    String title,
    String description,
    Set<Long> directionIds,
    Set<Long> topicIds,
    Long likeCount,
    Long dislikeCount,
    Long commentCount
) {}