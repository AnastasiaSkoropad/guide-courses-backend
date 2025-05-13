package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.dto.CreateCourseDto;
import com.courses.guidecourses.dto.UpdateCourseDto;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.CourseDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    // 1) Course → CourseDto
    @Mapping(target = "directionIds",
            expression = "java(course.getDirections().stream()" +
                    ".map(com.courses.guidecourses.entity.Direction::getId)" +
                    ".collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "topicIds",
            expression = "java(course.getTopics().stream()" +
                    ".map(com.courses.guidecourses.entity.Topic::getId)" +
                    ".collect(java.util.stream.Collectors.toSet()))")
    CourseDto toDto(Course course);

    // 2) CreateCourseDto → новий Course (без id, directions, topics)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "directions", ignore = true)
    @Mapping(target = "topics", ignore = true)
    Course toEntity(CreateCourseDto dto);

    // 3) UpdateCourseDto → існуючий Course (лише прості поля)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "directions", ignore = true)
    @Mapping(target = "topics", ignore = true)
    void updateFromDto(UpdateCourseDto dto, @MappingTarget Course course);

    // 4) Course → CourseDocument (для Elasticsearch)
    @Mapping(source = "id",          target = "id")
    @Mapping(source = "title",       target = "title")
    @Mapping(source = "description", target = "description")
    // збираємо коди напрямів у поле directions
    @Mapping(target = "directions",
            expression = "java(course.getDirections().stream()" +
                    ".map(com.courses.guidecourses.entity.Direction::getCode)" +
                    ".collect(java.util.stream.Collectors.toSet()))")
    // збираємо коди тем у поле topics
    @Mapping(target = "topics",
            expression = "java(course.getTopics().stream()" +
                    ".map(com.courses.guidecourses.entity.Topic::getCode)" +
                    ".collect(java.util.stream.Collectors.toSet()))")
    // likeCount, dislikeCount, commentCount MapStruct збере сам, бо імена збігаються
    CourseDocument toDocument(Course course);
}
