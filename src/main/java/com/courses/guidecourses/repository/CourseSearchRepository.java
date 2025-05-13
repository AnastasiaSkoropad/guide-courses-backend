package com.courses.guidecourses.repository;

import com.courses.guidecourses.entity.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseSearchRepository
       extends ElasticsearchRepository<CourseDocument, Long> {
  List<CourseDocument> findByTitleContaining(String text);
  List<CourseDocument> findByDirectionsContains(String directionCode);
  List<CourseDocument> findByTopicsContains(String topicCode);
}
