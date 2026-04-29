package com.superquizzettone.repository.quiz;

import com.superquizzettone.dto.QuizDTO;
import com.superquizzettone.model.Quiz;

import java.util.List;

public interface QuizRepositoryCustom {

    public List<Quiz> findByExample(QuizDTO quizExample);

    public List<Quiz> findByNameExample(String nameExample);
}
