package com.superquizzettone.repository.question;

import com.superquizzettone.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long>, JpaRepository<Question, Long> {


}
