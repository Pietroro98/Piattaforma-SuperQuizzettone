package com.superquizzettone.dto;
import com.superquizzettone.model.QuestionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MotivationDTO {

    private Long question_id;
    private String motivationRejection;
    private QuestionStatus status;
}
