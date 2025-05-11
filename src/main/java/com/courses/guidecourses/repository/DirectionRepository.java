package com.courses.guidecourses.repository;

import com.courses.guidecourses.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
    /**
     * Повернути всі напрями (Direction) для категорії з заданим кодом.
     * Використовується у DirectionService.findByCategoryCode.
     */
    List<Direction> findByCategory_Code(String categoryCode);

    /**
     * За потреби: знайти один Direction за його кодом і кодом категорії.
     */
    Optional<Direction> findByCategory_CodeAndCode(String categoryCode, String directionCode);
}
