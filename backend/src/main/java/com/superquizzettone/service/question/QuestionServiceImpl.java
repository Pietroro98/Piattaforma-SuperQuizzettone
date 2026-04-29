package com.superquizzettone.service.question;

import com.superquizzettone.model.Category;
import com.superquizzettone.model.Question;
import com.superquizzettone.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.StringTokenizer;

@Service
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> listAll(){
        return questionRepository.findAll();
    }

    public Question getSingleElement(Long id){
        return questionRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public void insertNew(Question question){
        questionRepository.save(question);
    }

    @Transactional
    public void remove(Long id){
        questionRepository.deleteById(id);
    }

    public List<Question> findByCategory(Category category){
        return questionRepository.findByCategory(category);
    }

    public List<Question> findByTag(String tag){
        return questionRepository.findByTag(tag);
    }

}
