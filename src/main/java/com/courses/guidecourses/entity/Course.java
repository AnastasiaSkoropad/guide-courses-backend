package com.courses.guidecourses.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Заголовок курсу — будь-якою мовою */
    @Column(nullable = false, length = 200)
    private String title;

    /** Опис курсу — будь-якою мовою */
    @Column(length = 2000)
    private String description;

    @Column(name = "preview_url", length = 500)
    private String previewUrl;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /** Курси можуть належати до будь-яких напрямів (Full Stack, Backend, English тощо) */
    @ManyToMany
    @JoinTable(
            name = "course_directions",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "direction_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"course_id","direction_id"})
    )
    private Set<Direction> directions = new HashSet<>();

    /** Курси можуть мати кілька Topic (Java, Spring або A1, B2 тощо) */
    @ManyToMany
    @JoinTable(
            name = "course_topics",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"course_id","topic_id"})
    )
    private Set<Topic> topics = new HashSet<>();

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(nullable = false)
    private Long dislikeCount = 0L;

    @Column(nullable = false)
    private Long commentCount = 0L;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}