package com.superquizzettone.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superquizzettone.model.Answer;
import com.superquizzettone.model.Question;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class AnswerDTO {

    private Long id;

    @NotBlank(message = "description non può essere nullo o vuoto")
    private String description;
    private boolean correct;

    public static AnswerDTO buildAnswerDTOfromModel(Answer answer) {
        AnswerDTO dto = new AnswerDTO();
        dto.setId(answer.getId());
        dto.setDescription(answer.getDescription());
        dto.setCorrect(answer.isCorrect());
        return dto;
    }


    public Answer buildAnswerModelFromDTO(Question question) {
        Answer answer = new Answer();
        answer.setId(this.id);
        answer.setDescription(this.description);
        answer.setCorrect(this.isCorrect());
        answer.setQuestion(question);
        return answer;
    }
}
