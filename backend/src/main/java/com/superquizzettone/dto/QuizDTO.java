package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.Quiz;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Set<CategoryDTO> categories;

    private Double totalPoints;

    private int questions;

    public QuizDTO(){}

    public static QuizDTO buildDTOFromModel(Quiz model){
        QuizDTO result = new QuizDTO();
        result.setId(model.getId());
        result.setDescription(model.getDescription());
        result.setName(model.getName());
        result.setQuizTime(model.getQuizTime());
        result.setCategories(model.getCategories().stream()
                .map(CategoryDTO::buildDTOFromModel)
                .collect(Collectors.toSet()));
        result.setTotalPoints(model.getTotalPoints());
        if(model.getQuestions() != null){
            model.getQuestions()
                    .stream()
                    .map(Question::getId)
                    .toList()
                    .size();
        } else {
            result.setQuestions(0);
        }
        return result;
    }

    public static Quiz buildModelFromDTO(QuizDTO dto) {
        Quiz result = new Quiz();
        result.setDescription(dto.getDescription());
        result.setName(dto.getName());
        result.setQuizTime(dto.getQuizTime());
        result.setTotalPoints(dto.getTotalPoints());
        result.setCategories(dto.getCategories().stream()
                .map(CategoryDTO::buildModelFromDTO)
                .collect(Collectors.toSet()));
        return result;
    }

    public static List<QuizDTO> buildListDTOFromModel(List<Quiz> listModel) {
        return listModel.stream()
                .map(QuizDTO::buildDTOFromModel)
                .toList();
    }

    public static List<Quiz> buildListModelFromDTO(List<QuizDTO> listDTO){
        return listDTO.stream()
                .map(QuizDTO::buildModelFromDTO)
                .toList();
    }

}
