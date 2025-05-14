package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.CourseComparisonDto;
import com.courses.guidecourses.dto.CriterionComparison;
import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.mapper.CourseMapper;
import com.courses.guidecourses.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class CourseComparisonService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseComparisonService(CourseRepository courseRepository,
                                   CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Transactional(readOnly = true)
    public CourseComparisonDto compare(List<Long> ids) {
        List<Course> courses = courseRepository.findAllById(ids);
        if (courses.size() != ids.size()) {
            throw new EntityNotFoundException("Не всі курси знайдені за переданими ID");
        }

        // Перетворюємо в DTO
        List<CourseDto> dtos = courses.stream()
            .map(courseMapper::toDto)
            .collect(Collectors.toList());

        Map<String, CriterionComparison> comps = new LinkedHashMap<>();

        // price: чим дешевше — тим краще
        Course cheapest = courses.stream().min(comparing(Course::getPrice)).get();
        Course mostExpensive = courses.stream().max(comparing(Course::getPrice)).get();
        comps.put("price", new CriterionComparison(cheapest.getId(), mostExpensive.getId()));

        // likeCount: більше — краще
        Course mostLiked = courses.stream().max(comparing(Course::getLikeCount)).get();
        Course leastLiked = courses.stream().min(comparing(Course::getLikeCount)).get();
        comps.put("likeCount", new CriterionComparison(mostLiked.getId(), leastLiked.getId()));

        // dislikeCount: менше — краще
        Course leastDisliked = courses.stream().min(comparing(Course::getDislikeCount)).get();
        Course mostDisliked  = courses.stream().max(comparing(Course::getDislikeCount)).get();
        comps.put("dislikeCount", new CriterionComparison(leastDisliked.getId(), mostDisliked.getId()));

        // commentCount: більше — краще
        Course mostCommented = courses.stream().max(comparing(Course::getCommentCount)).get();
        Course leastCommented = courses.stream().min(comparing(Course::getCommentCount)).get();
        comps.put("commentCount", new CriterionComparison(mostCommented.getId(), leastCommented.getId()));

        return new CourseComparisonDto(dtos, comps);
    }
}
