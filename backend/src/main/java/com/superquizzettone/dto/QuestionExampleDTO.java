package com.superquizzettone.dto;

import com.superquizzettone.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionExampleDTO {

    private Long id;
    private String tag;
    private String description;
    private QuestionStatus status;
    private Long categoryId;
    private QuestionType type;
    private User reviewedBy;

    public static QuestionExampleDTO buildQuestionExampleDTOFromModel(Question questionModel){

        QuestionExampleDTO result = new QuestionExampleDTO();
        result.setId(questionModel.getId());
        result.setDescription(questionModel.getDescription());
        result.setCategoryId(questionModel.getCategory().getId());
        result.setTag(questionModel.getTag());
        result.setStatus(questionModel.getStatus());
        result.setType(questionModel.getType());
        result.setReviewedBy(questionModel.getReviewedBy());



        return result;
    }

    public static List<QuestionExampleDTO> buildQuestionExampleDTOListFromModel(List<Question> models) {
        if (models == null || models.isEmpty()) return null;

        List<QuestionExampleDTO> result = new ArrayList<>();
        for (Question model : models) {
            result.add(buildQuestionExampleDTOFromModel(model));
        }

        return result;
    }

}
