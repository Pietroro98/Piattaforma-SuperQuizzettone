package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.Quiz;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class QuizQuestionsDTO {

    private Long id;

    @NotBlank(message="La descrizione non può essere nulla")
    private String description;

    @NotBlank(message = "Il nome non può essere nullo")
    private String name;

    @NotBlank(message = "Il tempo del quiz non può essere nullo")
    private String quizTime;

    private Double totalPoints;

    private List<QuestionDTO> questions = new ArrayList<>();


    public static QuizQuestionsDTO buildDTOFromModel(Quiz model){
        QuizQuestionsDTO result = new QuizQuestionsDTO();
        result.setId(model.getId());
        result.setDescription(model.getDescription());
        result.setName(model.getName());
        result.setQuizTime(model.getQuizTime());
        result.setTotalPoints(model.getTotalPoints());
        result.setQuestions(
                model.getQuestions()
                        .stream()
                        .map(QuestionDTO::buildQuestionDTOFromModel)
                        .toList()
        );
        return result;
    }

    /*
    public static Quiz buildModelFromDTO(QuizQuestionsDTO dto){
        Quiz result = new Quiz();
        result.setDescription(dto.getDescription());
        result.setName(dto.getName());
        result.setQuizTime(dto.getQuizTime());
        result.setTotalPoints(dto.getTotalPoints());
        result.setQuestions(
                dto.getQuestions()
                        .stream()
                        .map(Question::)
        );
        return result;
    }
     */


}
