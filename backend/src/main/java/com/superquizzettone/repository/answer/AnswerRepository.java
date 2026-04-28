package com.superquizzettone.repository.answer;

import com.superquizzettone.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepository extends CrudRepository<Answer, Long>, JpaRepository<Answer,Long> {


}
