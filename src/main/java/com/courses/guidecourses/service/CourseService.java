package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.CreateCourseDto;
import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.dto.UpdateCourseDto;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.Direction;
import com.courses.guidecourses.entity.Topic;
import com.courses.guidecourses.mapper.CourseMapper;
import com.courses.guidecourses.repository.CourseRepository;
import com.courses.guidecourses.repository.DirectionRepository;
import com.courses.guidecourses.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final DirectionRepository directionRepository;
    private final TopicRepository topicRepository;
    private final CourseMapper courseMapper;
    private final CourseSearchService courseSearchService;

    @Transactional(readOnly = true)
    public Page<CourseDto> findByFilter(String categoryCode,
                                        List<Long> directionIds,
                                        List<Long> topicIds,
                                        Pageable pageable) {
        Specification<Course> spec = Specification.where(null);

        if (categoryCode != null) {
            spec = spec.and((root, query, cb) -> {
                var d = root.join("directions");
                var c = d.join("category");
                return cb.equal(c.get("code"), categoryCode);
            });
        }

        if (directionIds != null && !directionIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                return root.join("directions").get("id").in(directionIds);
            });
        }

        if (topicIds != null && !topicIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                return root.join("topics").get("id").in(topicIds);
            });
        }

        return courseRepository.findAll(spec, pageable)
                .map(courseMapper::toDto);
    }

    @Transactional(readOnly = true)
    public CourseDto findById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + id));
    }

    public CourseDto create(CreateCourseDto dto) {
        Course course = new Course();
        course.setTitle(dto.title());
        course.setDescription(dto.description());

        // Fetch & attach directions
        Set<Direction> directions = directionRepository
                .findAllById(dto.directionIds())
                .stream().collect(Collectors.toSet());
        if (directions.size() != dto.directionIds().size()) {
            throw new EntityNotFoundException("One or more Directions not found");
        }
        course.setDirections(directions);

        // Fetch & attach topics
        Set<Topic> topics = topicRepository
                .findAllById(dto.topicIds())
                .stream().collect(Collectors.toSet());
        if (topics.size() != dto.topicIds().size()) {
            throw new EntityNotFoundException("One or more Topics not found");
        }
        course.setTopics(topics);

        Course saved = courseRepository.save(course);
        courseSearchService.indexCourse(saved);
        return courseMapper.toDto(saved);
    }

    public CourseDto update(Long id, UpdateCourseDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + id));

        course.setTitle(dto.title());
        course.setDescription(dto.description());

        // Оновити directions
        Set<Direction> directions = directionRepository
                .findAllById(dto.directionIds())
                .stream().collect(Collectors.toSet());
        if (directions.size() != dto.directionIds().size()) {
            throw new EntityNotFoundException("One or more Directions not found");
        }
        course.setDirections(directions);

        // Оновити topics
        Set<Topic> topics = topicRepository
                .findAllById(dto.topicIds())
                .stream().collect(Collectors.toSet());
        if (topics.size() != dto.topicIds().size()) {
            throw new EntityNotFoundException("One or more Topics not found");
        }
        course.setTopics(topics);

        Course updated = courseRepository.save(course);
        courseSearchService.indexCourse(updated);
        return courseMapper.toDto(updated);
    }

    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with id " + id);
        }
        courseRepository.deleteById(id);
        courseSearchService.deleteCourse(id);
    }
}
