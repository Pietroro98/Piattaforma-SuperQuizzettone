package com.superquizzettone.service.quiz;

import com.superquizzettone.dto.QuizDTO;
import com.superquizzettone.model.Quiz;
import com.superquizzettone.repository.quiz.QuizRepository;
import com.superquizzettone.web.api.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService{

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Quiz getQuiz(String quizName) {
        return quizRepository.findQuizByName(quizName);
    }

    @Override
    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(() ->new NotFoundException("Quiz non trovato con id: " + quizId ));
    }

    @Override
    @Transactional
    public Quiz insertNew(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    @Transactional
    public Quiz update(Quiz newQuiz) {
        return quizRepository.save(newQuiz);
    }

    @Override
    @Transactional
    public void delete(Long quizId) {
        quizRepository.deleteById(quizId);
    }

    @Override
    public List<Quiz> listAllQuiz() {
        return quizRepository.findAll();
    }

    @Override
    public List<Quiz> findQuizByExample(QuizDTO quizExample) {
        return quizRepository.findByExample(quizExample);
    }

    @Override
    public List<Quiz> findQuizsByName(String quizName) {
        return quizRepository.findByNameExample(quizName);
    }
}
