package com.courses.guidecourses.controller;

import com.courses.guidecourses.dto.AuthResponseDto;
import com.courses.guidecourses.dto.TokenResponse;
import com.courses.guidecourses.dto.UserDto;
import com.courses.guidecourses.dto.UserLoginDto;
import com.courses.guidecourses.dto.UserSignupDto;
import com.courses.guidecourses.service.AuthService;
import com.courses.guidecourses.service.KeycloakUserService;
import com.courses.guidecourses.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
  private final AuthService       authService;
  private final KeycloakUserService kcService;
  private final UserService userService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponseDto signup(@Valid @RequestBody UserSignupDto dto) {
    // 1) створити в Keycloak
    String kcId = kcService.createUser(dto);
    // 2) зберегти локально
    UserDto user = userService.createLocalUser(kcId, dto);
    // 3) відразу залогінити й отримати токен
    TokenResponse token = authService.login(new UserLoginDto(dto.getUsername(), dto.getPassword()));
    return AuthResponseDto.builder()
            .user(user)
            .token(token)
            .build();
  }

  @PostMapping("/login")
  public AuthResponseDto login(@Valid @RequestBody UserLoginDto dto) {
    TokenResponse token = authService.login(dto);
    // витягуємо keycloakId із токена або за nickname
    String kcId = authService.extractKeycloakId(token);
    UserDto user = userService.findByKeycloakId(kcId);
    return AuthResponseDto.builder()
            .token(token)
            .user(user)
            .build();
  }
}

