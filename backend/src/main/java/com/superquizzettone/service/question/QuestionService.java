package com.superquizzettone.service.question;

import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;

import java.util.List;

public interface QuestionService {

    List<Question> listAll();
    Question getSingleElement(Long id);
    Question update(QuestionDTO question);
    Question insertNew(QuestionDTO question);
    void remove(Long id);

    List<Question> getMyQuestions();
    List<Question> findByExample(QuestionDTO example);
    public Question rejectQuestion(MotivationDTO motivationDTO);
}
