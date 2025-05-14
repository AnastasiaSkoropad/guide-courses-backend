package com.courses.guidecourses.service;

import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.CourseDocument;
import com.courses.guidecourses.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseIndexService {
  private final com.courses.guidecourses.repository.CourseRepository courseRepository;
  private final ElasticsearchOperations esOps;
  private final CourseMapper courseMapper;

  // координати того ж індексу, що й у @Document(indexName = "courses")
  private final IndexCoordinates index = IndexCoordinates.of("courses");

  /** Перехоплює створення/оновлення курсу */
  public void index(Course course) {
    CourseDocument doc = courseMapper.toDocument(course);
    esOps.save(doc, index);
  }

  /** Видалити з індексу */
  public void delete(Long id) {
    esOps.delete(String.valueOf(id), CourseDocument.class);
  }

  /** Повне переіндексування при старті */
  @EventListener(ApplicationReadyEvent.class)
  public void reindexAll() {
    courseRepository.findAll()
            .stream()
            .map(courseMapper::toDocument)
            .forEach(doc -> esOps.save(doc, index));
  }
}
