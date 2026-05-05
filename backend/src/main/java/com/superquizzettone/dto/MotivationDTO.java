package com.superquizzettone.dto;
import com.superquizzettone.model.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MotivationDTO {

    private Long question_id;
    private String motivationRejection;
    private QuestionStatus status;
}
