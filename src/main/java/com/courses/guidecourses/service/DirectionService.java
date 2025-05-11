package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.DirectionDto;
import com.courses.guidecourses.entity.Direction;
import com.courses.guidecourses.mapper.DirectionMapper;
import com.courses.guidecourses.repository.DirectionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DirectionService {
    private final DirectionRepository directionRepository;
    private final DirectionMapper directionMapper;

    /**
     * Повертає всі напрями для заданої категорії (категорію за кодом перевіряє репозиторій).
     *
     * @param categoryCode код категорії, наприклад "IT" або "LANGUAGES"
     * @return список DirectionDto
     */
    public List<DirectionDto> findByCategoryCode(String categoryCode) {
        List<Direction> directions = directionRepository.findByCategory_Code(categoryCode);
        return directions.stream()
                         .map(directionMapper::toDto)
                         .collect(Collectors.toList());
    }

    /**
     * Повертає один Direction за кодом категорії та кодом напрямку, або кидає EntityNotFoundException.
     *
     * @param categoryCode код категорії
     * @param directionCode код напряму
     * @return DirectionDto
     */
    public DirectionDto findByCategoryAndCode(String categoryCode, String directionCode) {
        Direction direction = directionRepository
                .findByCategory_CodeAndCode(categoryCode, directionCode)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Direction not found for category=" + categoryCode +
                    " and code=" + directionCode));
        return directionMapper.toDto(direction);
    }
}
