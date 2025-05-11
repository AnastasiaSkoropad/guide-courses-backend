package com.courses.guidecourses.controller;

import com.courses.guidecourses.dto.CategoryDto;
import com.courses.guidecourses.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> listAll() {
        return categoryService.findAll(); // повертає всі категорії
    }
}
