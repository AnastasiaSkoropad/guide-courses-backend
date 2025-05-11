package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.DirectionDto;
import com.courses.guidecourses.entity.Direction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DirectionMapper {
    DirectionDto toDto(Direction direction);
}
