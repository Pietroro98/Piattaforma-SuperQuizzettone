package com.superquizzettone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "quiz_played")
@Getter
@Setter
@NoArgsConstructor
public class QuizPlayed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "score")
    private Double score;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "time_spent")
    private LocalTime timeSpent;
}
