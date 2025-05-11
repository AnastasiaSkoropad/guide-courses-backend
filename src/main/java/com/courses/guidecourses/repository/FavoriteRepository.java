package com.courses.guidecourses.repository;

import com.courses.guidecourses.entity.Favorite;
import com.courses.guidecourses.entity.User;
import com.courses.guidecourses.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    /**
     * Перевіряє, чи курс вже доданий у вибране користувачем.
     */
    boolean existsByUserAndCourse(User user, Course course);

    /**
     * Видаляє запис «вибране» для даного користувача та курсу.
     */
    void deleteByUserAndCourse(User user, Course course);

    /**
     * За потреби: повернути всі «вибрані» курси користувача.
     */
    List<Favorite> findByUser(User user);
}
