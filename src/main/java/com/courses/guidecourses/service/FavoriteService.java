package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.Favorite;
import com.courses.guidecourses.entity.User;
import com.courses.guidecourses.mapper.CourseMapper;
import com.courses.guidecourses.repository.CourseRepository;
import com.courses.guidecourses.repository.FavoriteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CourseMapper courseMapper;

    /**
     * Додає курс до вибраного для поточного користувача.
     * Якщо вже є — нічого не робить.
     *
     * @param courseId ідентифікатор курсу
     * @param username логін користувача (preferred_username з JWT)
     */
    @Transactional
    public void add(Long courseId, String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                    "User not found: " + username));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Course not found: " + courseId));

        boolean exists = favoriteRepository.existsByUserAndCourse(user, course);
        if (!exists) {
            Favorite fav = Favorite.builder()
                    .user(user)
                    .course(course)
                    .createdAt(Instant.now())
                    .build();
            favoriteRepository.save(fav);
        }
    }

    /**
     * Видаляє курс з вибраного для поточного користувача.
     * Якщо запису не було — нічого не робить.
     *
     * @param courseId ідентифікатор курсу
     * @param username логін користувача
     */
    @Transactional
    public void remove(Long courseId, String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                    "User not found: " + username));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Course not found: " + courseId));

        favoriteRepository.deleteByUserAndCourse(user, course);
    }

    public List<CourseDto> listFavorites(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        // знайти всі Favorite → отримати Course → мапити в CourseDto
        return favoriteRepository.findByUser(user)
                .stream()
                .map(Favorite::getCourse)
                .map(courseMapper::toDto)
                .toList();
    }
}
