package com.superquizzettone.service.answer;

import com.superquizzettone.dto.AnswerDTO;
import com.superquizzettone.model.Answer;
import com.superquizzettone.repository.answer.AnswerRepository;
import com.superquizzettone.web.api.exception.BadRequestException;
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
    public void update(Answer answer) {
        if (answer == null || answer.getId() == null){
            throw new BadRequestException("Risposta inserita risulta nulla");
        }
        answerRepository.save(answer);
    }

    @Transactional
    public void insertNew(Answer answer) {
        if (answer == null){
            throw new BadRequestException("Risposte inserite risultano nulle");
        }
        answerRepository.save(answer);
    }

    @Transactional
    public void remove(Long id){
        if (id == null){
            throw new BadRequestException("l'id risulta nullo");
        }
        answerRepository.deleteById(id);
    }
}
