package com.courses.guidecourses.repository;

import com.courses.guidecourses.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    /**
     * Повернути всі теми (Topic), прив’язані до конкретного Direction.
     * Використовується у TopicService.findByDirectionId.
     */
    List<Topic> findByDirections_Id(Long directionId);

    /**
     * За потреби: знайти всі теми за списком directionIds.
     */
    List<Topic> findByDirections_IdIn(List<Long> directionIds);
}
