package com.courses.guidecourses.repository;

import com.courses.guidecourses.dto.VoteType;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.CourseVote;
import com.courses.guidecourses.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<CourseVote, Long> {
    Optional<CourseVote> findByUserAndCourse(User user, Course course);
    long countByCourseAndType(Course course, VoteType type);
}
