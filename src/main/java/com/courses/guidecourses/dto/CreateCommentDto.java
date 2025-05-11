package com.courses.guidecourses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentDto {
    @NotBlank
    @Size(max = 1000)
    private String text;
}