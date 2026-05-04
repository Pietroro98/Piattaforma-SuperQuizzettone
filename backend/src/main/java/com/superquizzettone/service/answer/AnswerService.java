package com.superquizzettone.service.answer;

import com.superquizzettone.dto.AnswerDTO;
import com.superquizzettone.model.Answer;
import java.util.List;

public interface AnswerService {

    List<Answer> listAll();
    Answer getSingleElement(Long id);
    void update(AnswerDTO answer);
    void insertNew(AnswerDTO answer);
    void remove(Long id);

}
