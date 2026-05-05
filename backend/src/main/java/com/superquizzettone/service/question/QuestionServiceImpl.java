package com.superquizzettone.service.question;
import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.model.*;
import com.superquizzettone.repository.answer.AnswerRepository;
import com.superquizzettone.repository.category.CategoryRepository;
import com.superquizzettone.repository.question.QuestionRepository;
import com.superquizzettone.security.SanitizerUtil;
import com.superquizzettone.security.SecurityUtils;
import com.superquizzettone.service.utente.UserService;
import com.superquizzettone.web.api.exception.BadRequestException;
import com.superquizzettone.web.api.exception.NotAllowedException;
import com.superquizzettone.web.api.exception.NotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return questionRepository.findAll();
    }

    public Question getSingleElement(Long id){
        return questionRepository.findById(id).orElse(null);
    }

    @Transactional
    public Question update(Question question) {

        if (question == null || question.getId() == null ){
            throw new NotAllowedException("La question inserita risulta nulla.");
        }

        if (question.getAnswers().size() < 2 || question.getAnswers().size() > 10){
            throw new BadRequestException("Numero di risposte violato");
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

        if (question.getAnswers().size() < 2 || question.getAnswers().size() > 10){
            throw new BadRequestException("Numero di risposte violato");
        }

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

        question.setStatus(QuestionStatus.IN_REVIEW);
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

        if (userLoggato.getRoles().stream().anyMatch(role -> Role.ROLE_REVIEWER.equals(role.getCode()))) {
            return questionRepository.findByCreatedByIdAndStatus(
                    userLoggato.getId(),
                    QuestionStatus.IN_REVIEW
            );
        }

        return questionRepository.findByCreatedById(userLoggato.getId());
    }

    public List<Question> findByExample(Question example){

        if (example == null){
            throw new BadRequestException("Elemento di ricerca invalido (example nulla)");
        }

        Map<String, Object> parameterMap= new HashMap<>();
        List<String> whereClauses = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder("select q from Question q left join q.answers where q.id = q.id");

        if(StringUtils.isNotEmpty(example.getDescription())){
            whereClauses.add("q.description like :description");
            parameterMap.put("description", "%" + example.getDescription() + "%");
        }

        if (StringUtils.isNotEmpty(example.getTag())) {
            whereClauses.add(" q.tag like :tag ");
            parameterMap.put("tag", "%" + example.getTag() + "%");
        }
        if (StringUtils.isNotEmpty(example.getMotivationRejection())) {
            whereClauses.add(" q.motivationRejection like :motivationRejection ");
            parameterMap.put("motivationRejection", "%" + example.getMotivationRejection() + "%");
        }
        if (example.getCategory() !=null && example.getCategory().getId() != null) {
            whereClauses.add(" q.category.id = :categoryId ");
            parameterMap.put("categoryId", example.getCategory().getId());
        }

        if (example.getAnswers() != null && !example.getAnswers().isEmpty()) {
            String answerDescription = example.getAnswers().get(0).getDescription();
            if (StringUtils.isNotEmpty(answerDescription)) {
                whereClauses.add(" a.description like :answerDescription ");
                parameterMap.put("answerDescription", "%" + answerDescription + "%");
            }
        }

        if (example.getStatus() != null ){
            whereClauses.add(" q.status like :status");
            parameterMap.put("status",  example.getStatus());
        }

        if (example.getType() != null) {
            whereClauses.add(" q.type = :type ");
            parameterMap.put("type", example.getType());
        }

        queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
        queryBuilder.append(org.apache.commons.lang3.StringUtils.join(whereClauses, " and "));

        TypedQuery<Question> typedQuery = entityManager.createQuery(queryBuilder.toString(), Question.class);

        for (String key : parameterMap.keySet()) {
            typedQuery.setParameter(key, parameterMap.get(key));
        }

        return typedQuery.getResultList();
    }


    @Transactional
    public Question rejectQuestion(MotivationDTO motivationDTO) {

        if (motivationDTO == null || motivationDTO.getQuestion_id() == null) {
            throw new BadRequestException("L'id della question risulta nullo");
        }

        String sanitizedMotivation = SanitizerUtil.sanitize(motivationDTO.getMotivationRejection());
        if (sanitizedMotivation == null || sanitizedMotivation.isBlank()) {
            throw new BadRequestException("La motivazione del rifiuto risulta nulla o vuota");
        }

        Long idQuestion = motivationDTO.getQuestion_id();
        Question question = questionRepository.findById(idQuestion)
                .orElseThrow(() -> new NotFoundException("Question non trovata con id: " + idQuestion));

        question.setMotivationRejection(sanitizedMotivation);

        if(motivationDTO.getStatus() == QuestionStatus.IN_REVIEW){
            throw new NotAllowedException("Errore durante l'inserimento dello stato question");
        }

        if (motivationDTO.getStatus() == QuestionStatus.DRAFT) {
            question.setStatus(QuestionStatus.DRAFT);
        } else if(motivationDTO.getStatus() == QuestionStatus.REJECTED) {
            question.setStatus(QuestionStatus.REJECTED);
        } else {
            question.setStatus(QuestionStatus.ACCEPTED);
        }
        return questionRepository.save(question);
    }
}
