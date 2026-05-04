package com.superquizzettone.service.question;

import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;

import java.util.List;

public interface QuestionService {

    List<Question> listAll();
    Question getSingleElement(Long id);
    void update(QuestionDTO question);
    void insertNew(QuestionDTO question);
    void remove(Long id);

    List<Question> findByCategory(Category category);
    List<Question> findByTag(String tag);
    List<Question> findByExample(QuestionDTO example);
    public Question rejectQuestion(MotivationDTO motivationDTO);
}
