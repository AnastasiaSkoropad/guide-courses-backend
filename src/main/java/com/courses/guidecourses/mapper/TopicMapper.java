package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.TopicDto;
import com.courses.guidecourses.entity.Topic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicDto toDto(Topic topic);
}
