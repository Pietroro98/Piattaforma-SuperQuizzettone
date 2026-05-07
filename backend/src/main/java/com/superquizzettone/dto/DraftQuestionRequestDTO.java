package com.superquizzettone.dto;

import com.superquizzettone.model.Answer;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.model.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DraftQuestionRequestDTO {

    private String description;

    private List<AnswerDTO> answers;

    private CategoryDTO category;

    private String tag;
    private QuestionStatus status;

    private QuestionType type;

    public Question toModel() {
        Question result = new Question();
        result.setDescription(description);
        if(category != null){
            result.setCategory(CategoryDTO.buildModelFromDTO(category));
        }

        result.setTag(tag);
        result.setStatus(status);
        result.setType(type);

        if (answers != null) {
            List<Answer> mappedAnswers = answers.stream()
                    .map(AnswerDTO::buildAnswerModelFromDTO)
                    .toList();
            mappedAnswers.forEach(answer -> answer.setQuestion(result));
            result.setAnswers(mappedAnswers);
        }

        return result;
    }
}
