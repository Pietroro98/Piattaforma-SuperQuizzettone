package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class AnswerDTO {

    private Long id;
    private String description;
    private boolean correct;

    public static AnswerDTO buildAnswerDTOfromModel(Answer answer) {
        AnswerDTO dto = new AnswerDTO();
        dto.setId(answer.getId());
        dto.setDescription(answer.getDescription());
        dto.setCorrect(answer.isCorrect());
        return dto;
    }
}
