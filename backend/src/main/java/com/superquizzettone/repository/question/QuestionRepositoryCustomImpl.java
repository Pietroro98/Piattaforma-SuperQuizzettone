package com.superquizzettone.repository.question;

import com.superquizzettone.dto.QuestionExampleDTO;
import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.repository.category.CategoryRepository;
import com.superquizzettone.web.api.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Question> findByExample(QuestionExampleDTO example) {
        Map<String, Object> paramaterMap = new HashMap<String, Object>();
        List<String> whereClauses = new ArrayList<String>();

        StringBuilder queryBuilder = new StringBuilder("select q from Question q where q.id = q.id and q.status = :status");

        paramaterMap.put("status", QuestionStatus.ACCEPTED);

        if (StringUtils.isNotEmpty(example.getTag())) {
            whereClauses.add("q.tag like :tag");
            paramaterMap.put("tag", "%" + example.getTag() + "%");
        }

        if (StringUtils.isNotEmpty(example.getDescription())) {
            whereClauses.add("q.description like :description");
            paramaterMap.put("description", "%" + example.getDescription() + "%");
        }

        if (example.getStatus() != null) {
            whereClauses.add("q.status = :status");
            paramaterMap.put("status", example.getStatus());
        }

        if (example.getCategoryId() != null) {

            Category c = categoryRepository.findById(example.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Categoria non trovata"));
            whereClauses.add("q.category = :category");
            paramaterMap.put("category", c);
        }

        if (example.getType() != null) {
            whereClauses.add("q.type = :type");
            paramaterMap.put("type", example.getType());
        }

        if (example.getReviewedBy() != null) {
            whereClauses.add("q.reviewedBy = :reviewedBy");
            paramaterMap.put("reviewedBy", example.getReviewedBy());
        }

        queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
        queryBuilder.append(StringUtils.join(whereClauses, " and "));
        TypedQuery<Question> typedQuery = entityManager.createQuery(queryBuilder.toString(), Question.class);

        for (String key : paramaterMap.keySet()) {
            typedQuery.setParameter(key, paramaterMap.get(key));
        }

        return typedQuery.getResultList();
    }
}
