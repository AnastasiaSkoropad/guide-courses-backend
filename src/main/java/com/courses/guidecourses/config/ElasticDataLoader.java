package com.courses.guidecourses.config;

import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.repository.CourseRepository;
import com.courses.guidecourses.service.CourseSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")  // щоби не запускати в тестах
@RequiredArgsConstructor
public class ElasticDataLoader implements CommandLineRunner {

    private final CourseRepository courseRepo;
    private final CourseSearchService searchService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        int page = 0;
        int size = 50; // розмір сторінки
        Page<Course> chunk;
        do {
            chunk = courseRepo.findAll(PageRequest.of(page, size));
            chunk.forEach(searchService::indexCourse);
            page++;
        } while (!chunk.isLast());

        long total = courseRepo.count();
        System.out.printf("→ Індексація в Elasticsearch завершена, прокіндексовано %d курсів%n", total);
    }
}