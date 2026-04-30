package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.Quiz;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class QuizDTO {

    private Long id;

    @NotBlank(message="La descrizione non può essere nulla")
    private String description;

    @NotBlank(message = "Il nome non può essere nullo")
    private String name;

    @NotBlank(message = "Il tempo del quiz non può essere nullo")
    private String quizTime;

    private Double totalPoints;

    private List<Long> questions = new ArrayList<>();

    public QuizDTO(){}

    public static QuizDTO buildDTOFromModel(Quiz model){
        QuizDTO result = new QuizDTO(
                model.getId(),
                model.getDescription(),
                model.getName(),
                model.getQuizTime(),
                model.getTotalPoints(),
                model.getQuestions()
                        .stream()
                        .map(Question::getId)
                        .toList()
        );
        return result;
    }

    public static Quiz buildModelFromDTO(QuizDTO dto) {
        Quiz result = new Quiz();
        result.setDescription(dto.getDescription());
        result.setName(dto.getName());
        result.setQuizTime(dto.getQuizTime());
        result.setTotalPoints(dto.getTotalPoints());
        return result;
    }



}
