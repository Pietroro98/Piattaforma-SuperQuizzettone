package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.model.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class QuestionResponseDTO {

    private Long id;
    private String description;
    private List<AnswerDTO> answers;
    private CategoryDTO category;
    private String tag;
    private String motivationRejection;
    private QuestionStatus status;
    private QuestionType type;
    private UserSummaryDTO reviewedBy;
    private LocalDateTime approvalDate;

    public static QuestionResponseDTO fromModel(Question questionModel) {
        QuestionResponseDTO result = new QuestionResponseDTO();
        result.setId(questionModel.getId());
        result.setDescription(questionModel.getDescription());
        result.setCategory(CategoryDTO.buildDTOFromModel(questionModel.getCategory()));
        result.setTag(questionModel.getTag());
        result.setMotivationRejection(questionModel.getMotivationRejection());
        result.setStatus(questionModel.getStatus());
        result.setType(questionModel.getType());
        result.setReviewedBy(UserSummaryDTO.fromModel(questionModel.getReviewedBy()));
        result.setApprovalDate(questionModel.getApprovalDate());

        if (questionModel.getAnswers() != null) {
            result.setAnswers(
                    questionModel.getAnswers()
                            .stream()
                            .map(AnswerDTO::buildAnswerDTOfromModel)
                            .toList()
            );
        } else {
            result.setAnswers(Collections.emptyList());
        }

        return result;
    }
}
