package com.courses.guidecourses.controller;

import com.courses.guidecourses.dto.VoteDto;
import com.courses.guidecourses.dto.VoteResultDto;
import com.courses.guidecourses.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses/{courseId}/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public VoteResultDto vote(
        @PathVariable Long courseId,
        @Valid @RequestBody VoteDto dto,
        @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getClaim("preferred_username");
        return voteService.vote(courseId, username, dto.type());
    }
}
