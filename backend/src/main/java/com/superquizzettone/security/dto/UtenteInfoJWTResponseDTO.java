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

    private String nome;
    private String cognome;
    private String type = "Bearer";
    private String username;
    private UserState stato;
    private List<String> roles;
    private List<QuizPlayed> attempts;
    private LocalDate dataRegistrazione;
    private Double totalPoint;

    public UtenteInfoJWTResponseDTO(String nome, String cognome, String username, UserState stato, List<String> roles, List<QuizPlayed> attempts, LocalDate dataRegistrazione, Double totalPoint) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.stato = stato;
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
        this.dataRegistrazione = dataRegistrazione;
    }
}
