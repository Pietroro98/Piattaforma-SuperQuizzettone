package com.superquizzettone.repository.question;

import com.superquizzettone.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {


}
