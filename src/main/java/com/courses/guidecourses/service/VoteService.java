package com.courses.guidecourses.service;

import com.courses.guidecourses.dto.VoteResultDto;
import com.courses.guidecourses.dto.VoteType;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.CourseVote;
import com.courses.guidecourses.entity.User;
import com.courses.guidecourses.repository.CourseRepository;
import com.courses.guidecourses.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    /**
     * Голос користувача за курс: like/dislike.
     * Якщо повторити той самий тип голосу — скасовує його.
     * Якщо змінити тип (з like на dislike чи навпаки) — оновлює запис.
     * Після операції повертає оновлені лічильники like/dislike.
     *
     * @param courseId ідентифікатор курсу
     * @param username логін користувача (preferred_username)
     * @param type     тип голосу (LIKE або DISLIKE)
     * @return VoteResultDto із підрахунком лайків та дизлайків
     */
    @Transactional
    public VoteResultDto vote(Long courseId, String username, VoteType type) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));

        // Знайти існуючий голос
        voteRepository.findByUserAndCourse(user, course).ifPresentOrElse(existing -> {
            if (existing.getType() == type) {
                // Якщо той самий тип — видаляємо (toggle off)
                voteRepository.delete(existing);
            } else {
                // Інший тип — оновлюємо
                existing.setType(type);
                existing.setCreatedAt(Instant.now());
                voteRepository.save(existing);
            }
        }, () -> {
            // Якщо голосу ще не було — створюємо новий
            CourseVote vote = CourseVote.builder()
                    .user(user)
                    .course(course)
                    .type(type)
                    .createdAt(Instant.now())
                    .build();
            voteRepository.save(vote);
        });

        // Підрахунок підсумків
        long likes    = voteRepository.countByCourseAndType(course, VoteType.LIKE);
        long dislikes = voteRepository.countByCourseAndType(course, VoteType.DISLIKE);

        course.setLikeCount(voteRepository.countByCourseAndType(course, VoteType.LIKE));
        course.setDislikeCount(voteRepository.countByCourseAndType(course, VoteType.DISLIKE));
        courseRepository.save(course);

        return new VoteResultDto((int) likes, (int) dislikes);
    }
}
