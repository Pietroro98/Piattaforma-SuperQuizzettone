package com.superquizzettone.service.quiz;

import com.superquizzettone.dto.QuizDTO;
import com.superquizzettone.dto.QuizTrainingSampleDTO;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.model.Quiz;
import com.superquizzettone.repository.category.CategoryRepository;
import com.superquizzettone.repository.question.QuestionRepository;
import com.superquizzettone.repository.quiz.QuizRepository;
import com.superquizzettone.security.SecurityUtils;
import com.superquizzettone.web.api.exception.BadRequestException;
import com.superquizzettone.web.api.exception.InsufficientDurationException;
import com.superquizzettone.web.api.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final SecurityUtils securityUtils;
    private final CategoryRepository categoryRepository;

    public QuizServiceImpl(QuizRepository quizRepository, QuestionRepository questionRepository, SecurityUtils securityUtils, CategoryRepository categoryRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.securityUtils = securityUtils;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Quiz getQuiz(String quizName) {
        return quizRepository.findQuizByName(quizName).orElseThrow(() -> new NotFoundException("Quiz non trovato con nome:" + quizName));
    }

    @Override
    public Quiz getQuizById(Long quizId) {
        if (quizId == null) {
            throw new BadRequestException("Attenzione, l'id non può essere nullo");
        }
        return quizRepository.findById(quizId).orElseThrow(() -> new NotFoundException("Quiz non trovato con id: " + quizId));
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
        if (result == null) {
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

    @Override
    @Transactional
    public QuizDTO createTrainingQuiz(QuizTrainingSampleDTO dto) {

        Quiz saved = quizRepository.save(buildQuizFromQuizTrainingSampleDTO(dto));
        for (Question q : saved.getQuestions()) {
            System.out.println(q.getDescription());
        }
        return QuizDTO.buildDTOFromModel(saved);
    }

    private Quiz buildQuizFromQuizTrainingSampleDTO(QuizTrainingSampleDTO dto) {
        if (dto.getDuration().isEmpty() || dto.getDuration().isBlank())
            throw new BadRequestException("inserire un valore valido come durata: non può essere nulla, vuota o diversa da un numero");
        Quiz quiz = new Quiz();
        quiz.setName("Quiz di allenamento " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));

        Set<Long> categoryIds = dto.getCategoryIds();
        List<Question> listaDomande = questionRepository.findTrainingQuestions(categoryIds, dto.getNumberOfQuestions());

        if (dto.getNumberOfQuestions() > listaDomande.size())
            throw new BadRequestException("Selezionare un numero di domande inferiore in quanto non ne sono presenti più di "
                    + listaDomande.size() + " affini alle categorie scelte sul database");

        Collections.shuffle(listaDomande);

        List<Question> listaDomandeLimitate = listaDomande.subList(0, dto.getNumberOfQuestions());

        Set<Category> categorie = new HashSet<>();
        for (Long idCategoria : dto.getCategoryIds()) {
            Category temp = categoryRepository.findById(idCategoria).orElse(null);
            if(temp != null)
                categorie.add(temp);
        }

        quiz.setCategories(categorie);
        quiz.setQuestions(listaDomandeLimitate);
        quiz.setTotalPoints(dto.getCorrectAnswer() * dto.getNumberOfQuestions());
        quiz.setCreatedBy(securityUtils.getLoggedUser());
        quiz.setDescription("Test di allenamento");
        quiz.setQuizTime(dto.getDuration());
        return quiz;
    }

}
