package com.courses.guidecourses.controller;

import com.courses.guidecourses.dto.CourseComparisonDto;
import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.service.CourseComparisonService;
import com.courses.guidecourses.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final CourseComparisonService comparisonService;

    /** POST /api/users/me/favorites/{courseId} */
    @PostMapping("/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFavorite(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getClaim("preferred_username");
        favoriteService.add(courseId, username);
    }

    /** DELETE /api/users/me/favorites/{courseId} */
    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getClaim("preferred_username");
        favoriteService.remove(courseId, username);
    }

    /** GET /api/users/me/favorites */
    @GetMapping
    public List<CourseDto> listFavorites(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getClaim("preferred_username");
        return favoriteService.listFavorites(username);
    }

    @GetMapping("/compare")
    public CourseComparisonDto compare(
            @RequestParam("ids") List<Long> ids
    ) {
        if (ids.size() < 2 || ids.size() > 3) {
            throw new IllegalArgumentException("Потрібно передати 2 або 3 ID курсів");
        }
        return comparisonService.compare(ids);
    }
}
