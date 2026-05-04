package com.superquizzettone.model;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Duration;
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
    @JoinColumn(name = "quiz_id")
    private List<Question> questions = new ArrayList<>();

    @Column(name = "quiz_time")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
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

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    public String getQuizTime() {
        if (quizTime == null) {
            return null;
        }

        long totalSeconds = quizTime.getSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setQuizTime(String quizTime) {
        this.quizTime = parseMinutesSeconds(quizTime);
    }

    public static Duration parseMinutesSeconds(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String[] parts = value.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato durata non valido. Atteso mm:ss");
        }

        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);

        if (seconds < 0 || seconds > 59) {
            throw new IllegalArgumentException("I secondi devono essere tra 00 e 59");
        }

        return Duration.ofMinutes(minutes).plusSeconds(seconds);
    }
}
