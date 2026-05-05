package com.superquizzettone.repository.question;

import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCreatedById(Long idUser);

    List<Question> findByCreatedByIdAndStatus(Long idUser, QuestionStatus status);
}
