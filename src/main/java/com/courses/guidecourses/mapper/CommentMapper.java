package com.courses.guidecourses.mapper;

import com.courses.guidecourses.dto.CommentDto;
import com.courses.guidecourses.dto.CreateCommentDto;
import com.courses.guidecourses.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id",     ignore = true)
    @Mapping(target = "course", ignore = true)
    Comment toEntity(CreateCommentDto dto);

    CommentDto toDto(Comment comment);
}


