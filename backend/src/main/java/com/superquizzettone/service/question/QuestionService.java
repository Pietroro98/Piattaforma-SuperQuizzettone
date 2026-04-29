package com.superquizzettone.service.question;

import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;

import java.util.List;

public interface QuestionService {

    List<Question> listAll();
    Question getSingleElement(Long id);
    void update(Question question);
    void insertNew(Question question);
    void remove(Long id);

    List<Question> findByCategory(Category category);
    List<Question> findByTag(String tag);
}
