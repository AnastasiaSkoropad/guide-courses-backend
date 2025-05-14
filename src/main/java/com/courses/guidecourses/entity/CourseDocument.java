package com.courses.guidecourses.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String title;

  @Field(type = FieldType.Text)
  private String description;

  @Field(type = FieldType.Text)
  private Set<String> directions;

  @Field(type = FieldType.Text)
  private Set<String> topics;

  @CompletionField(maxInputLength = 100)
  private Completion titleSuggest;

  @CompletionField(maxInputLength = 100)
  private Completion directionSuggest;

  @CompletionField(maxInputLength = 100)
  private Completion topicSuggest;

  @Field(type = FieldType.Long)
  private Long likeCount;

  @Field(type = FieldType.Long)
  private Long dislikeCount;

  @Field(type = FieldType.Long)
  private Long commentCount;
}

