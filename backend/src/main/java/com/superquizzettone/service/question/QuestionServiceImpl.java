package com.superquizzettone.service.question;
import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.dto.QuestionExampleDTO;

import com.superquizzettone.dto.ReviewQuestionRequestDTO;
import com.superquizzettone.model.*;
import com.superquizzettone.repository.answer.AnswerRepository;
import com.superquizzettone.repository.category.CategoryRepository;
import com.superquizzettone.repository.question.QuestionRepository;
import com.superquizzettone.security.SanitizerUtil;
import com.superquizzettone.security.SecurityUtils;
import com.superquizzettone.service.utente.UserService;
import com.superquizzettone.web.api.exception.BadRequestException;
import com.superquizzettone.web.api.exception.ConflictException;
import com.superquizzettone.web.api.exception.NotAllowedException;
import com.superquizzettone.web.api.exception.NotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    public List<Question> listAll(){
        return questionRepository.findByStatus(QuestionStatus.ACCEPTED);
    }

    public Question getSingleElement(Long id){
        return questionRepository.findById(id).orElse(null);
    }

    @Transactional
    public Question update(Question question) {

        if (question == null || question.getId() == null ){
            throw new NotAllowedException("La question inserita risulta nulla.");
        }

        validateAnswers(question);

        Question existing = questionRepository.findById(question.getId())
                .orElseThrow(() -> new NotFoundException("Question non trovata con id: " + question.getId()));

        if (existing.getStatus() == QuestionStatus.UNDER_REVIEWER) {
            throw new NotAllowedException("La question è attualmente presa in carico da un reviewer e non può essere modificata");
        }

        return questionRepository.save(question);
    }

    @Transactional
    public Question insertNew(Question question){

        if (question == null ){
            throw new NotAllowedException("La question inserita risulta nulla.");
        }

        if (question.getId() != null) {
            throw new BadRequestException("Una nuova question non puo avere un id valorizzato");
        }

        if (question.getAnswers() == null) {
            throw new BadRequestException("La question deve contenere almeno 4 risposte");
        }

        validateAnswers(question);

        User userLoggato = userService.findByUsername(SecurityUtils.getUsername());
        if (userLoggato == null) {
            throw new NotFoundException("Utente autenticato non trovato");
        }

        if (question.getCategory() != null) {
            Category requestCategory = question.getCategory();
            Category category;

            if (requestCategory.getId() != null) {
                category = categoryRepository.findById(requestCategory.getId())
                        .orElseThrow(() -> new NotFoundException("Categoria non trovata con id: " + requestCategory.getId()));
            } else {
                if (requestCategory.getName() == null || requestCategory.getName().isBlank()) {
                    throw new BadRequestException("Il nome della categoria non puo essere nullo o vuoto");
                }

                category = categoryRepository.findByName(requestCategory.getName())
                        .orElseGet(() -> {
                            requestCategory.setQuestionStatus(QuestionStatus.IN_REVIEW);
                            return categoryRepository.save(requestCategory);
                        });
            }

            if (category.getQuestionStatus() == QuestionStatus.REJECTED) {
                throw new BadRequestException("La categoria esiste già ma risulta rifiutata e non puo essere riutilizzata");
            }

            question.setCategory(category);
        }

        List<Answer> answers = new ArrayList<>(question.getAnswers());
        question.setAnswers(new ArrayList<>());

        if (question.getStatus() == null) {
            question.setStatus(QuestionStatus.IN_REVIEW);
        } else if (question.getStatus() != QuestionStatus.DRAFT && question.getStatus() != QuestionStatus.IN_REVIEW) {
            throw new BadRequestException("Lo stato iniziale della question deve essere DRAFT oppure IN_REVIEW");
        }
        question.setCreatedBy(userLoggato);

        Question savedQuestion = questionRepository.save(question);

        for (Answer answer : answers) {
            answer.setId(null);
            answer.setQuestion(savedQuestion);
        }

        List<Answer> savedAnswers = answerRepository.saveAll(answers);
        savedQuestion.setAnswers(savedAnswers);

        return savedQuestion;
    }

    @Transactional
    @Override
    public void remove(Long id){

        if(id == null){
            throw new BadRequestException("l'id della domanda richiesta è null");
        }


        questionRepository.deleteById(id);
    }

    @Override
    public List<Question> getMyQuestions() {
        User userLoggato = userService.findByUsername(SecurityUtils.getUsername());
        if (userLoggato == null) {
            throw new NotFoundException("Utente autenticato non trovato");
        }

       boolean verifica =  userLoggato.getRoles().stream().anyMatch(role -> Role.ROLE_REVIEWER.equals(role.getCode()));

        if (verifica) {
            return questionRepository.findByStatus(
                    QuestionStatus.IN_REVIEW
            );
        }

        return questionRepository.findMyQuestionsByUserId(userLoggato.getId());
    }

    @Override
    @Transactional
    public List<Question> findByExample(QuestionExampleDTO example) {
        List<Question> result = questionRepository.findByExample(example);
        if(result == null){
            throw new NotFoundException("Risultato nullo");
        }
        return result;
    }

    /**
     * Metodo che consente al reviewer di prendere in carico una question per la revisione,
     * modificandone lo stato da IN_REVIEW a UNDER_REVIEWER e associandola al reviewer che l'ha presa in carico.
     * @param questionId
     * @return
     */
    @Override
    @Transactional
    public Question claimForReview(Long questionId) {
        if(questionId == null){
            throw new BadRequestException("L'id della question risulta nullo");
        }

        User userLoggato = userService.findByUsername(SecurityUtils.getUsername());
        if(userLoggato == null){
            throw new NotFoundException("Utente autenticato non trovato");
        }

        Question question = questionRepository.findById(questionId).orElseThrow(() ->
                new NotFoundException("Question non trovata con id: " + questionId));

        if (question.getStatus() == QuestionStatus.UNDER_REVIEWER) {
            if(question.getReviewedBy() != null && question.getReviewedBy().getId().equals(userLoggato.getId())){
                return question;
            }
            throw new NotAllowedException("La question è già stata presa in carico da un altro reviewer");
        }

        int updated = questionRepository.claimQuestion(
                questionId,
                userLoggato,
                QuestionStatus.IN_REVIEW,
                QuestionStatus.UNDER_REVIEWER
        );

        if(updated == 0) {
            throw new ConflictException("La question non è più disponibile per la presa in carico");
        }

        return questionRepository.findById(questionId).orElseThrow(() ->
                new NotFoundException("Question non trovata con id: " + questionId));
    }


    /**
     * Metodo che consente al reviewer di visualizzare la lista delle question
     * che sono attualmente in stato IN_REVIEW e che quindi sono disponibili
     * per essere prese in carico per la revisione.
     * @return
     */
    @Override
    public List<Question> getQuestionsAvailableForReview() {
        return questionRepository.findByStatus(QuestionStatus.IN_REVIEW);
    }

    /**
     * Metodo che consente al reviewer di visualizzare la lista
     * delle question che ha preso in carico e che sono attualmente in stato UNDER_REVIEWER.
     * @return
     */
    @Override
    public List<Question> getMyClaimedQuestions() {
        User userLoggato = userService.findByUsername(SecurityUtils.getUsername());
        if (userLoggato == null) {
            throw new NotFoundException("Utente autenticato non trovato");
        }

        return questionRepository.findByStatusAndReviewedById(
                QuestionStatus.UNDER_REVIEWER,
                userLoggato.getId());
    }

    /**
     * Metodo che consente al reviewer di revisionare
     * una question precedentemente presa in carico,
     * modificandone lo stato finale e inserendo eventualmente una motivazione in caso di reject.
     * @param questionId
     * @param reviewQuestionRequestDTO
     * @return
     */
    @Override
    @Transactional
    public Question reviewQuestion(Long questionId, ReviewQuestionRequestDTO reviewQuestionRequestDTO) {
        if (questionId == null) {
            throw new BadRequestException("L'id della question risulta nullo");
        }

        if (reviewQuestionRequestDTO == null || reviewQuestionRequestDTO.getStatus() == null) {
            throw new BadRequestException("Lo stato finale della question risulta nullo");
        }

        User userLoggato = userService.findByUsername(SecurityUtils.getUsername());
        if (userLoggato == null) {
            throw new NotFoundException("Utente autenticato non trovato");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question non trovata con id: " + questionId));

        if (question.getStatus() != QuestionStatus.UNDER_REVIEWER) {
            throw new NotAllowedException("La question non è attualmente presa in carico");
        }

        if (question.getReviewedBy() == null || !question.getReviewedBy().getId().equals(userLoggato.getId())) {
            throw new NotAllowedException("La question è assegnata a un altro reviewer");
        }

        QuestionStatus targetStatus = reviewQuestionRequestDTO.getStatus();
        if (targetStatus != QuestionStatus.DRAFT
                && targetStatus != QuestionStatus.ACCEPTED
                && targetStatus != QuestionStatus.REJECTED) {
            throw new BadRequestException("Stato finale non valido");
        }

        String sanitizedMotivation = SanitizerUtil.sanitize(reviewQuestionRequestDTO.getMotivationRejection());
        if ((targetStatus == QuestionStatus.DRAFT || targetStatus == QuestionStatus.REJECTED)
                && (sanitizedMotivation == null || sanitizedMotivation.isBlank())) {
            throw new BadRequestException("La motivazione è obbligatoria");
        }

        question.setMotivationRejection(sanitizedMotivation);
        question.setStatus(targetStatus);

        if (targetStatus == QuestionStatus.ACCEPTED) {
            question.setApprovalDate(LocalDateTime.now());
            question.setReviewedBy(userLoggato);
        } else {
            question.setApprovalDate(null);
            question.setReviewedBy(null);
        }

        return questionRepository.save(question);
    }

    /**
     * Metodo per validare le risposte di una question in base al tipo della question stessa,
     * verificando che siano rispettate le regole di business definite per ogni tipologia.
     * @param question
     */
    private void validateAnswers(Question question) {
        if (question.getType() == null) {
            throw new BadRequestException("Il tipo della question non può essere nullo");
        }

        if (question.getAnswers() == null || question.getAnswers().size() < 2 || question.getAnswers().size() > 10) {
            throw new BadRequestException("Numero di risposte violato");
        }

        long correctAnswers = question.getAnswers().stream()
                .filter(Answer::isCorrect)
                .count();

        if (question.getType() == QuestionType.SINGOLA) {
            if (correctAnswers != 1) {
                throw new BadRequestException("Per una question di tipo SINGOLA deve esserci una sola risposta corretta");
            }
            return;
        }

        if (question.getType() == QuestionType.MULTIPLA) {
            if (correctAnswers < 2 || correctAnswers >= question.getAnswers().size()) {
                throw new BadRequestException("Per una question di tipo MULTIPLA devono esserci almeno 2 risposte corrette e almeno una errata");
            }
            return;
        }

        if (question.getType() == QuestionType.VEROFALSO) {
            if (question.getAnswers().size() != 2) {
                throw new BadRequestException("Per una question di tipo VEROFALSO devono esserci esattamente 2 risposte");
            }

            if (correctAnswers != 1) {
                throw new BadRequestException("Per una question di tipo VEROFALSO deve esserci una sola risposta corretta e una sola errata");
            }
        }
    }
}
