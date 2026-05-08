package com.superquizzettone.repository.quiz;

import com.superquizzettone.model.Category;
import com.superquizzettone.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QuizRepository extends QuizRepositoryCustom, JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q " +
            "JOIN q.attempts a " +
            "WHERE a.user.id = :id")
    public List<Quiz> findAllByUserId(@Param("id") Long id);

    @Query("SELECT q FROM Quiz q " +
            "WHERE q.name = :name")
    public Optional<Quiz> findQuizByName(@Param("name") String name);

    @Query("SELECT DISTINCT q FROM Quiz q " +
            "LEFT JOIN FETCH q.questions qu " +
            "LEFT JOIN FETCH qu.answers " +
            "WHERE q.id IN :ids")
    List<Quiz> findAllWithQuestionsAndAnswers(@Param("ids") List<Long> ids);

    Page<Quiz> findAll(Pageable pageable);

    Boolean existsByName(String name);

    Optional<Quiz> findByName(String name);

}
