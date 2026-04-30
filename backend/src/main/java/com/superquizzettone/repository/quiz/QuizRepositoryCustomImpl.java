package com.superquizzettone.repository.quiz;

import com.superquizzettone.dto.QuizDTO;
import com.superquizzettone.model.Quiz;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizRepositoryCustomImpl implements QuizRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Quiz> findByExample(QuizDTO example) {
        Map<String, Object> paramaterMap = new HashMap<String, Object>();
        List<String> whereClauses = new ArrayList<String>();

        StringBuilder queryBuilder = new StringBuilder("select q from quiz q where q.id = q.id ");

        if(StringUtils.isNotEmpty(example.getName())){
            whereClauses.add("q.name like :name");
            paramaterMap.put("name", "%" + example.getName() + "%");
        }

        if(StringUtils.isNotEmpty(example.getDescription())){
            whereClauses.add("q.description like :description");
            paramaterMap.put("description", "%" + example.getDescription() +"%");
        }

        if(example.getTotalPoints() != null){
            whereClauses.add("q.total_points <= :totalPoints");
            paramaterMap.put("totalPoints", example.getTotalPoints());
        }

        if(StringUtils.isNotEmpty(example.getQuizTime())){
            Duration time = Quiz.parseMinutesSeconds(example.getQuizTime());
            whereClauses.add("q.quiz_time <= :quizTime");
            paramaterMap.put("quizTime", time);
        }
        queryBuilder.append(!whereClauses.isEmpty()?" and ":"");
        queryBuilder.append(StringUtils.join(whereClauses, " and "));
        TypedQuery<Quiz> typedQuery =entityManager.createQuery(queryBuilder.toString(), Quiz.class);

        for (String key : paramaterMap.keySet()) {
            typedQuery.setParameter(key, paramaterMap.get(key));
        }

        return typedQuery.getResultList();
    }

    @Override
    public List<Quiz> findByNameExample(String nameExample) {
        TypedQuery<Quiz> typedQuery =entityManager.createQuery("select q from quiz where q.name like :name", Quiz.class);

        typedQuery.setParameter("name", "%" + nameExample + "%");

        return typedQuery.getResultList();
    }
}
