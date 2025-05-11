package com.courses.guidecourses.controller;

import com.courses.guidecourses.dto.CourseDto;
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
}
