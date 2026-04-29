package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;
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
    private String description;
    private List<AnswerDTO> answers;
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
