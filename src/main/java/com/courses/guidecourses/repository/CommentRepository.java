package com.courses.guidecourses.repository;

import com.courses.guidecourses.entity.Comment;
import com.courses.guidecourses.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByCourseId(Long courseId, Pageable pageable);
    long countByCourse(Course course);
}
