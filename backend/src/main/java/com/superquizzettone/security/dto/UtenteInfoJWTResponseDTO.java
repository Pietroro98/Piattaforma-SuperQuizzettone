package com.superquizzettone.security.dto;
import com.superquizzettone.model.Quiz;
import com.superquizzettone.model.QuizPlayed;
import com.superquizzettone.model.UserState;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UtenteInfoJWTResponseDTO {

    private String name;
    private String surname;
    private String type = "Bearer";
    private String username;
    private UserState state;
    private List<String> roles;
    private List<QuizPlayed> attempts;
    private LocalDate registrationDate;
    private Double totalPoint;

    public UtenteInfoJWTResponseDTO(String name, String surname, String username, UserState state, List<String> roles, List<QuizPlayed> attempts, LocalDate registrationDate, Double totalPoint) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.state = state;
        this.totalPoint = totalPoint;
        this.roles = roles;
        this.attempts = attempts == null ? List.of() : attempts.stream().map(attempt -> {
            QuizPlayed quizPlayed = new QuizPlayed();
            Quiz quiz = new Quiz();
            if (attempt.getQuiz() != null) {
                quiz.setName(attempt.getQuiz().getName());
            }
            quizPlayed.setQuiz(quiz);
            quizPlayed.setScore(attempt.getScore());
            quizPlayed.setCompletionDate(attempt.getCompletionDate());
            quizPlayed.setTimeSpent(attempt.getTimeSpent());
            return quizPlayed;
        }).toList();
        this.registrationDate = registrationDate;
    }
}
