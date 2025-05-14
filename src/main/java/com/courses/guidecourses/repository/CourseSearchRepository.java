package com.courses.guidecourses.repository;

import com.courses.guidecourses.entity.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSearchRepository
        extends ElasticsearchRepository<CourseDocument, Long> { }