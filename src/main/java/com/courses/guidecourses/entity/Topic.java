package com.courses.guidecourses.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Назва для UI, локалізована, наприклад "Java", "A1", "Business" */
    @Column(nullable = false, length = 120)
    private String title;

    /** Усі Directions, до яких належить цей Topic */
    @ManyToMany(mappedBy = "topics")
    private Set<Direction> directions = new HashSet<>();

    /** Усі Courses, що прив’язані до цього Topic */
    @ManyToMany(mappedBy = "topics")
    private Set<Course> courses = new HashSet<>();
}