package com.courses.guidecourses.dto;

import java.util.List;
import java.util.Map;

public record CourseComparisonDto(
    List<CourseDto> courses,
    Map<String, CriterionComparison> comparisons
) {}