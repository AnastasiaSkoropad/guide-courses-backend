package com.courses.guidecourses.controller;

import com.courses.guidecourses.dto.DirectionDto;
import com.courses.guidecourses.service.DirectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories/{categoryCode}/directions")
@RequiredArgsConstructor
public class DirectionController {
    private final DirectionService directionService;

    @GetMapping
    public List<DirectionDto> listByCategory(@PathVariable String categoryCode) {
        return directionService.findByCategoryCode(categoryCode);
    }
}
