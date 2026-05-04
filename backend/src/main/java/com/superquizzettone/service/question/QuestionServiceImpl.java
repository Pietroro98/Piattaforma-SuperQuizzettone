package com.superquizzettone.service.question;

import ch.qos.logback.core.util.StringUtil;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.model.User;
import com.superquizzettone.repository.question.QuestionRepository;
import com.superquizzettone.security.SecurityUtils;
import com.superquizzettone.web.api.exception.BadRequestException;
import com.superquizzettone.web.api.exception.ForbiddenException;
import com.superquizzettone.web.api.exception.NotAllowedException;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SecurityUtils securityUtils;

    public List<Question> listAll(){
        return questionRepository.findAll();
    }

    public Question getSingleElement(Long id){
        return questionRepository.findById(id).orElse(null);
    }

    @Transactional
    public Question update(QuestionDTO question) {

        if (SecurityUtils.isPlayer() || SecurityUtils.isReviewer() || SecurityUtils.isAdministrator() ){
            throw new ForbiddenException("Non puoi modificare una domanda, non sei un writer");
        }

        if (question == null){
            throw new BadRequestException("Le modifiche inserite sono nulle");
        }

        if (question.getAnswers().size() < 2 || question.getAnswers().size() > 10){
            throw new BadRequestException("Numero di risposte violato");
        }

        Question model = question.buildQuestionModel(true);
        return questionRepository.save(model);
    }

    @Transactional
    public Question insertNew(QuestionDTO question){

        if (SecurityUtils.isPlayer() || SecurityUtils.isReviewer() || SecurityUtils.isAdministrator()){
            throw new ForbiddenException("Non puoi inserire una domanda, non sei un writer");
        }

        if (question == null){
            throw new NotAllowedException("La question inserita risulta nulla, riprova scemo");
        }

        if (question.getType() == null) {
            throw new BadRequestException("Il type della question non puo essere nullo");
        }

        if (question.getAnswers().size() < 2 || question.getAnswers().size() > 10){
            throw new BadRequestException("Numero di risposte violato");
        }

        question.setStatus(QuestionStatus.IN_REVIEW);
        Question model = question.buildQuestionModel(true);
        return questionRepository.save(model);
    }

    @Transactional
    public void remove(Long id){

        if(id == null){
            throw new BadRequestException("l'id della domanda richiesta è null");
        }


        questionRepository.deleteById(id);
    }

    public List<Question> findByCategory(Category category){

        if (category == null){
            throw new BadRequestException("La category risulta nulla");
        }
        return questionRepository.findByCategory(category);
    }

    public List<Question> findByTag(String tag){

        if (tag == null){
            throw new BadRequestException("Il tag della domanda risulta nullo");
        }
        return questionRepository.findByTag(tag);
    }

    public List<Question> findByExample(QuestionDTO example){

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
}
