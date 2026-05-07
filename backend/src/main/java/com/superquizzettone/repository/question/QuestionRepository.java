package com.superquizzettone.repository.question;

import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends QuestionRepositoryCustom, JpaRepository<Question, Long> {
    @Query("select q from Question q where q.createdBy.id = :userId")
    List<Question> findMyQuestionsByUserId(@Param("userId") Long userId);

    List<Question> findByStatus( QuestionStatus status);
}
