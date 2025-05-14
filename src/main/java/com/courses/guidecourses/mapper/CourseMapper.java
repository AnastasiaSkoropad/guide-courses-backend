package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "directionIds", expression = "java(course.getDirections().stream().map(d->d.getId()).collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "topicIds",     expression = "java(course.getTopics().stream().map(t->t.getId()).collect(java.util.stream.Collectors.toSet()))")
    CourseDto toDto(Course course);
}