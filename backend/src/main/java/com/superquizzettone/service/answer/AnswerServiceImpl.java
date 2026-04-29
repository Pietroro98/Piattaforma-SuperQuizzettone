package com.superquizzettone.service.answer;

import com.superquizzettone.model.Answer;
import com.superquizzettone.repository.answer.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService{

    @Autowired
    private AnswerRepository answerRepository;

    public List<Answer> listAll(){
        return answerRepository.findAll();
    }

    public Answer getSingleElement(Long id){
        return answerRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(Answer answer){
        answerRepository.save(answer);
    }

    @Transactional
    public void insertNew(Answer answer){
        answerRepository.save(answer);
    }

    @Transactional
    public void remove(Long id){
        answerRepository.deleteById(id);
    }
}
