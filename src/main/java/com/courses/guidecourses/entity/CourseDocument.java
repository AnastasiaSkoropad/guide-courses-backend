package com.courses.guidecourses.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Set;

@Data
@Document(indexName = "courses")
public class CourseDocument {
  @Id
  private Long id;
  private String title;
  private String description;
  private Set<String> directions;   // просто коди/назви напрямів
  private Set<String> topics;       // коди/назви тем
  private Long likeCount;
  private Long dislikeCount;
  private Long commentCount;
}
