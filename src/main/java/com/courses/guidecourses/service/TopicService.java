package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.TopicDto;
import com.courses.guidecourses.entity.Topic;
import com.courses.guidecourses.mapper.TopicMapper;
import com.courses.guidecourses.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    /**
     * Повертає всі теми (Topic) для заданого напряму (Direction).
     *
     * @param directionId ідентифікатор Direction
     * @return список TopicDto
     */
    public List<TopicDto> findByDirectionId(Long directionId) {
        List<Topic> topics = topicRepository.findByDirections_Id(directionId);
        return topics.stream()
                     .map(topicMapper::toDto)
                     .collect(Collectors.toList());
    }
}
