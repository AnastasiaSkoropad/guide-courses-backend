package com.courses.guidecourses.controller;

import com.courses.guidecourses.dto.TopicDto;
import com.courses.guidecourses.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/directions/{directionId}/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public List<TopicDto> listByDirection(@PathVariable Long directionId) {
        return topicService.findByDirectionId(directionId);
    }
}
