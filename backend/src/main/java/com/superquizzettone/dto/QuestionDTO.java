package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class QuestionDTO {

    private Long id;

    @NotBlank(message = "description non può essere vuota")
    private String description;

    @NotNull(message = "answers non può essere nullo")
    @Size(min = 2, message = "answers deve contenere almeno 2 elementi")
    @Valid
    private List<AnswerDTO> answers;

    @NotNull(message = "category non può essere nullo")
    private Category category;
    private String tag;
    private String motivationRejection;


    public static QuestionDTO buildQuestionDTOFromModel(Question questionModel){

        QuestionDTO result = new QuestionDTO();
        result.setId(questionModel.getId());
        result.setDescription(questionModel.getDescription());
        result.setCategory(questionModel.getCategory());
        result.setTag(questionModel.getTag());
        result.setMotivationRejection(questionModel.getMotivationRejection());

        if (questionModel.getAnswers() != null) {
            result.setAnswers(
                    questionModel.getAnswers()
                            .stream()
                            .map(AnswerDTO::buildAnswerDTOfromModel)
                            .collect(Collectors.toList())
            );
        } else {
            result.setAnswers(Collections.emptyList());
        }

        return result;
    }

}
