package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.Role;
import com.courses.guidecourses.dto.UserDto;
import com.courses.guidecourses.dto.UserSignupDto;
import com.courses.guidecourses.entity.User;
import com.courses.guidecourses.mapper.UserMapper;
import com.courses.guidecourses.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Створює локального користувача після реєстрації в Keycloak,
     * повертає створений UserDto.
     */
    @Transactional
    public UserDto createLocalUser(String keycloakId, UserSignupDto dto) {
        User user = User.builder()
                .keycloakId(keycloakId)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .avatarUrl(dto.getAvatarUrl())
                .roles(Set.of(Role.ROLE_USER))
                .build();

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    /**
     * Повертає профіль користувача по його Keycloak ID.
     * @throws EntityNotFoundException, якщо користувача не знайдено
     */
    @Transactional(readOnly = true)
    public UserDto findByKeycloakId(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "User not found with keycloakId: " + keycloakId));
        return userMapper.toDto(user);
    }

    /**
     * Повертає профіль користувача по його внутрішньому ID.
     * @throws EntityNotFoundException, якщо користувача не знайдено
     */
    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "User not found with id: " + id));
        return userMapper.toDto(user);
    }

    /**
     * Шукає сутність User по nickname (preferred_username).
     * Використовується внутрішньо (FavoriteService, CommentService тощо).
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
