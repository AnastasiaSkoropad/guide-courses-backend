package com.courses.guidecourses.dto;

import java.util.List;

public record SuggestionDto(
    List<String> titles,
    List<String> directions,
    List<String> topics
  ) {}