package com.superquizzettone.repository.question;

import com.superquizzettone.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCreatedById(Long idUser);
}
