package com.courses.guidecourses.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "directions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"category_id", "code"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Код, наприклад "BACKEND", "FULL_STACK", "ENGLISH" */
    @Column(nullable = false, length = 60)
    private String code;

    /** Назва для UI, локалізована, наприклад "Backend", "Full Stack", "English" */
    @Column(nullable = false, length = 120)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** З курсу в N напрямків */
    @ManyToMany(mappedBy = "directions")
    private Set<Course> courses = new HashSet<>();

    /** Які теми (Topic) стосуються цього напряму */
    @ManyToMany
    @JoinTable(
        name = "direction_topics",
        joinColumns = @JoinColumn(name = "direction_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"direction_id","topic_id"})
    )
    private Set<Topic> topics = new HashSet<>();
}