package com.superquizzettone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "quiz")
@Getter
@Setter
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany
    @JoinColumn(name = "quiz_id",  nullable = false)
    private List<Question> questions = new ArrayList<>();

    @Column(name = "quiz_time")
    private Duration quizTime;

    @Column(name = "total_points")
    private Double totalPoints;

    @ManyToMany
    @JoinTable(
            name = "quiz_category",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "quiz")
    private List<QuizPlayed> attempts = new ArrayList<>();
}
