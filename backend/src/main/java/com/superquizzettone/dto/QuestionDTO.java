package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.*;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private CategoryDTO category;
    private String tag;
    private String motivationRejection;
    private QuestionStatus status;
    private QuestionType type;
    private User createdBy;
    private User reviewedBy;
    private LocalDateTime approvalDate;


    public static QuestionDTO buildQuestionDTOFromModel(Question questionModel){

        QuestionDTO result = new QuestionDTO();
        result.setId(questionModel.getId());
        result.setDescription(questionModel.getDescription());
        result.setCategory(CategoryDTO.buildDTOFromModel(questionModel.getCategory()));
        result.setTag(questionModel.getTag());
        result.setMotivationRejection(questionModel.getMotivationRejection());
        result.setStatus(questionModel.getStatus());
        result.setType(questionModel.getType());
        result.setCreatedBy(questionModel.getCreatedBy());
        result.setReviewedBy(questionModel.getReviewedBy());
        result.setApprovalDate(questionModel.getApprovalDate());

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

    public Question buildQuestionModel( boolean includeAnswers){
        Question result = new Question();
        result.setId(id);
        result.setTag(tag);
        result.setDescription(description);
        result.setCategory(CategoryDTO.buildModelFromDTO(category));
        result.setMotivationRejection(motivationRejection);
        result.setStatus(status);
        result.setType(type);
        result.setCreatedBy(createdBy);
        result.setReviewedBy(reviewedBy);
        result.setApprovalDate(approvalDate);

        if (includeAnswers && this.answers != null){

            result.setAnswers
                    (this.answers
                            .stream()
                            .map(answerDTO -> answerDTO.buildAnswerModelFromDTO(answerDTO))
                            .collect(Collectors.toList()));
        }
        else {
            result.setAnswers(Collections.emptyList());
        }
        return result;
    }

}
