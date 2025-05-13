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
    private final CourseIndexService indexService;

    @Transactional(readOnly = true)
    public Page<CourseDto> findByFilter(String categoryCode,
                                        List<Long> directionIds,
                                        List<Long> topicIds,
                                        Pageable pageable) {
        Specification<Course> spec = Specification.where(null);
        // ... ваші умови фільтрації, без змін ...
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
        // 1) Мапимо прості поля
        Course course = courseMapper.toEntity(dto);

        // 2) Завантажуємо й прив'язуємо directions
        Set<Direction> directions = directionRepository.findAllById(dto.directionIds())
                .stream().collect(Collectors.toSet());
        if (directions.size() != dto.directionIds().size()) {
            throw new EntityNotFoundException("One or more Directions not found");
        }
        course.setDirections(directions);

        // 3) Завантажуємо й прив'язуємо topics
        Set<Topic> topics = topicRepository.findAllById(dto.topicIds())
                .stream().collect(Collectors.toSet());
        if (topics.size() != dto.topicIds().size()) {
            throw new EntityNotFoundException("One or more Topics not found");
        }
        course.setTopics(topics);

        // 4) Зберігаємо та індексуємо в ES
        Course saved = courseRepository.save(course);
        indexService.index(saved);

        return courseMapper.toDto(saved);
    }

    public CourseDto update(Long id, UpdateCourseDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id " + id));

        // 1) Апдейтимо прості поля
        courseMapper.updateFromDto(dto, course);

        // 2) Оновлюємо зв’язки directions
        Set<Direction> directions = directionRepository.findAllById(dto.directionIds())
                .stream().collect(Collectors.toSet());
        if (directions.size() != dto.directionIds().size()) {
            throw new EntityNotFoundException("One or more Directions not found");
        }
        course.setDirections(directions);

        // 3) Оновлюємо зв’язки topics
        Set<Topic> topics = topicRepository.findAllById(dto.topicIds())
                .stream().collect(Collectors.toSet());
        if (topics.size() != dto.topicIds().size()) {
            throw new EntityNotFoundException("One or more Topics not found");
        }
        course.setTopics(topics);

        // 4) Зберігаємо та ре-індексуємо
        Course updated = courseRepository.save(course);
        indexService.index(updated);

        return courseMapper.toDto(updated);
    }

    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with id " + id);
        }
        courseRepository.deleteById(id);
        indexService.delete(id);
    }
}
