package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.CategoryDto;
import com.courses.guidecourses.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
}
