package com.superquizzettone.service.answer;

import com.superquizzettone.model.Answer;
import java.util.List;

public interface AnswerService {

    List<Answer> listAll();
    Answer getSingleElement(Long id);
    void update(Answer answer);
    void insertNew(Answer answer);
    void remove(Long id);

}
