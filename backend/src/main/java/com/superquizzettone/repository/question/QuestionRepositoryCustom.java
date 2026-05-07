package com.superquizzettone.repository.question;


import com.superquizzettone.dto.QuestionExampleDTO;
import com.superquizzettone.model.Question;

import java.util.List;

public interface QuestionRepositoryCustom {

    public List<Question> findByExample(QuestionExampleDTO questionExample);

}
