package com.superquizzettone.service.question;

import ch.qos.logback.core.util.StringUtil;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;
import com.superquizzettone.repository.question.QuestionRepository;
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

    public List<Question> listAll(){
        return questionRepository.findAll();
    }

    public Question getSingleElement(Long id){
        return questionRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public void insertNew(Question question){
        questionRepository.save(question);
    }

    @Transactional
    public void remove(Long id){
        questionRepository.deleteById(id);
    }

    public List<Question> findByCategory(Category category){
        return questionRepository.findByCategory(category);
    }

    public List<Question> findByTag(String tag){
        return questionRepository.findByTag(tag);
    }

    public List<QuestionDTO> findByExample(Question example){

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


        queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
        queryBuilder.append(org.apache.commons.lang3.StringUtils.join(whereClauses, " and "));

        TypedQuery<Question> typedQuery = entityManager.createQuery(queryBuilder.toString(), Question.class);

        for (String key : parameterMap.keySet()) {
            typedQuery.setParameter(key, parameterMap.get(key));
        }

        return typedQuery.getResultList()
                .stream()
                .map(QuestionDTO::buildQuestionDTOFromModel)
                .collect(Collectors.toList());
    }
}
