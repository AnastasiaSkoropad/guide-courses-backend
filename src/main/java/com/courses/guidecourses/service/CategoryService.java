package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.CategoryDto;
import com.courses.guidecourses.entity.Category;
import com.courses.guidecourses.mapper.CategoryMapper;
import com.courses.guidecourses.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Повертає всі категорії у вигляді DTO
     */
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Знаходить категорію за кодом або кидає EntityNotFoundException
     * @param code код категорії, наприклад "IT" або "LANGUAGES"
     */
    public Category getByCode(String code) {
        return categoryRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category not found with code: " + code
                ));
    }
}
