package com.superquizzettone.service.question;

import com.superquizzettone.dto.ReviewQuestionRequestDTO;
import com.superquizzettone.model.Question;

import java.util.List;

public interface QuestionService {

    List<Question> listAll();
    Question getSingleElement(Long id);
    Question update(Question question);
    Question insertNew(Question question);
    void remove(Long id);

    List<Question> getMyQuestions();
    List<Question> findByExample(Question example);
    Question claimForReview(Long questionId);
    List<Question> getQuestionsAvailableForReview();
    List<Question> getMyClaimedQuestions();
    Question reviewQuestion(Long questionId, ReviewQuestionRequestDTO reviewQuestionRequestDTO);
}
