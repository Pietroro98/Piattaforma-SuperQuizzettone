package com.superquizzettone.repository.quiz;

import com.superquizzettone.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends QuizRepositoryCustom, JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q " +
            "JOIN q.attempts a " +
            "WHERE a.user.id = :id")
    public List<Quiz> findAllByUserId(@Param("id") Long id);

    @Query("SELECT q FROM Quiz " +
            "WHERE q.name = :name")
    public Quiz findQuizByName(@Param("name") String name);

}
