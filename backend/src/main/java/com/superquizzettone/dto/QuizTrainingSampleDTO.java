package com.superquizzettone.dto;

import com.superquizzettone.model.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizTrainingSampleDTO {

    @Min(1)
    @Max(60)
    private Integer numberOfQuestions;
    private Set<Long> categoryIds;
    private String duration;
    private Double correctAnswer;
    private Double wrongAnswer;
    private Double notGivenAnswer;

}
