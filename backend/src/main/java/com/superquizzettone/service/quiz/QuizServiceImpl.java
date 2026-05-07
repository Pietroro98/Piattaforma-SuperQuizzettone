package com.superquizzettone.service.quiz;

import com.superquizzettone.dto.QuizDTO;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.Quiz;
import com.superquizzettone.repository.quiz.QuizRepository;
import com.superquizzettone.web.api.exception.BadRequestException;
import com.superquizzettone.web.api.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService{

    private final QuizRepository quizRepository;

    public QuizServiceImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public Quiz getQuiz(String quizName) {
        return quizRepository.findQuizByName(quizName).orElseThrow(() -> new NotFoundException("Quiz non trovato con nome:" + quizName));
    }

    @Override
    public Quiz getQuizById(Long quizId) {
        if(quizId == null) {
            throw new BadRequestException("Attenzione, l'id non può essere nullo");
        }
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
        quizRepository.findById(newQuiz.getId())
                .orElseThrow(() -> new NotFoundException("Quiz non trovato con id: " + newQuiz.getId()));
        return quizRepository.save(newQuiz);
    }

    @Override
    @Transactional
    public void delete(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new NotFoundException("Quiz non trovato con id: " + quizId);
        }
        quizRepository.deleteById(quizId);
    }

    @Override
    public List<Quiz> listAllQuiz() {
        return quizRepository.findAll();
    }

    @Override
    public List<Quiz> findQuizByExample(QuizDTO quizExample) {
        List<Quiz> result = quizRepository.findByExample(quizExample);
        if(result == null){
            throw new NotFoundException("Risultato nullo");
        }
        return result;
    }

    @Override
    public List<Quiz> findQuizsByName(String quizName) {
        return quizRepository.findByNameExample(quizName);
    }

    @Override
    public Page<Quiz> listAllQuizPaginated(Pageable pageable) {
        Page<Quiz> quizPage = quizRepository.findAll(pageable);
        List<Long> ids = quizPage.getContent()
                .stream()
                .map(Quiz::getId)
                .toList();

        List<Quiz> quizWithDetails = quizRepository.findAllWithQuestionsAndAnswers(ids);

        return new PageImpl<>(quizWithDetails, pageable, quizPage.getTotalElements());
    }
}
