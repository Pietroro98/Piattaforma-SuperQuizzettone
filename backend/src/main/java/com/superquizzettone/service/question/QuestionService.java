package com.superquizzettone.service.question;

import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.dto.QuestionExampleDTO;
import com.superquizzettone.model.Question;

import java.util.List;

public interface QuestionService {

    List<Question> listAll();
    Question getSingleElement(Long id);
    Question update(Question question);
    Question insertNew(Question question);
    void remove(Long id);

    List<Question> getMyQuestions();
    //List<Question> findByExample(Question example);
    List<Question> findByExample(QuestionExampleDTO example);
    Question rejectQuestion(MotivationDTO motivationDTO);
}
