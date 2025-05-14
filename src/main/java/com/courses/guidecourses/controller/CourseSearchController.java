package com.courses.guidecourses.controller;

import com.courses.guidecourses.entity.CourseDocument;
import com.courses.guidecourses.service.CourseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class CourseSearchController {
  private final CourseSearchService searchService;

  @GetMapping("/courses")
  public List<CourseDocument> search(@RequestParam String string) {
    return searchService.searchAll(string);
  }
}
