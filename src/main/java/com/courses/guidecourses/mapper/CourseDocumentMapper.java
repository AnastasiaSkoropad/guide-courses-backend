package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.CourseDocument;
import com.courses.guidecourses.entity.Direction;
import com.courses.guidecourses.entity.Topic;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseDocumentMapper {
    @Mapping(target = "directions", ignore = true)
    @Mapping(target = "topics",     ignore = true)
    CourseDocument toDocument(Course course);

    CourseDto toDto(CourseDocument doc);

    @AfterMapping
    default void enrichCollections(Course course, @MappingTarget CourseDocument doc) {
        doc.setDirections(
                course.getDirections()
                        .stream()
                        .map(Direction::getTitle)
                        .collect(Collectors.toSet())
        );
        doc.setTopics(
                course.getTopics()
                        .stream()
                        .map(Topic::getTitle)
                        .collect(Collectors.toSet())
        );
    }
}

