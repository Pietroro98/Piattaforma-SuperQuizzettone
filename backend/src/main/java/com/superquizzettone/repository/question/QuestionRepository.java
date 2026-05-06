package com.superquizzettone.repository.question;

import com.superquizzettone.model.Question;
import com.superquizzettone.model.QuestionStatus;
import com.superquizzettone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q from Question q where q.createdBy.id = :userId")
    List<Question> findMyQuestionsByUserId(@Param("userId") Long userId);

    // query ATOMICA PER L?UPDATE DELLO STATO ALLA PRESA IN CARICO
    @Modifying
    @Query("""
    update Question q
    set q.status = :newStatus, q.reviewedBy = :reviewer
    where q.id = :questionId and q.status = :currentStatus
    """)
    int claimQuestion(
            @Param("questionId") Long questionId,
            @Param("reviewer") User reviewer,
            @Param("currentStatus") QuestionStatus currentStatus,
            @Param("newStatus") QuestionStatus newStatus
    );


    // query per LE MIE DOMANDE PRESE IN CARICO
    @Query("""
    select q
    from Question q
    where q.status = :status and q.reviewedBy.id = :reviewerId
""")
    List<Question> findByStatusAndReviewedById(
            @Param("status") QuestionStatus status,
            @Param("reviewerId") Long reviewerId
    );

    List<Question> findByStatus( QuestionStatus status);
}
