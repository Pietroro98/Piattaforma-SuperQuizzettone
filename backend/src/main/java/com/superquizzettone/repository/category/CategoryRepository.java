package com.superquizzettone.repository.category;

import com.superquizzettone.model.Category;
import com.superquizzettone.model.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    List<Category> findByQuestionStatus(QuestionStatus status);
}
