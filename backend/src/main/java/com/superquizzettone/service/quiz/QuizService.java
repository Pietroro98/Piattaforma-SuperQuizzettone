package com.superquizzettone.service.quiz;

import com.superquizzettone.dto.QuizDTO;
import com.superquizzettone.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuizService {

    public Quiz insertNew(Quiz quiz);

    public List<Quiz> listAllQuiz();

    public Quiz update(Quiz newQuiz);

    public void delete(Long quizId);

    public Quiz getQuiz(String quizName);

    public Quiz getQuizById(Long quizId);

    public List<Quiz> findQuizByExample(QuizDTO quizExample);

    public List<Quiz> findQuizsByName(String quizName);

    public Page<Quiz> listAllQuizPaginated(Pageable pageable);


}
