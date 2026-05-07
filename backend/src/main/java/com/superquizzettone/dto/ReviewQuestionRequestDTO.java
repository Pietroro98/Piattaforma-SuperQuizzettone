package com.superquizzettone.dto;

import com.superquizzettone.model.QuestionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewQuestionRequestDTO {

    @NotNull(message = "Lo stato finale non può essere nullo")
    private QuestionStatus status;

    private String motivationRejection;
}
