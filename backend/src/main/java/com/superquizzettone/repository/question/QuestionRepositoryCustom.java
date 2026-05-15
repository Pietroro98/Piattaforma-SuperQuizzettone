package com.superquizzettone.repository.question;


import com.superquizzettone.dto.QuestionExampleDTO;
import com.superquizzettone.model.Question;

import java.util.List;
import java.util.Set;

public interface QuestionRepositoryCustom {

    public List<Question> findByExample(QuestionExampleDTO questionExample);

    List<Question> findTrainingQuestions(
            Set<Long> categoryIds,
            Integer numberOfQuestions
    );

}
