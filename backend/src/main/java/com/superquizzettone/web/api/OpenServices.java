package com.superquizzettone.web.api;

import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.service.question.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/open-services")
public class OpenServices {

    private final QuestionService questionService;

    public OpenServices(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/all-questions")
    public ResponseEntity<ResponseJSON<List<QuestionDTO>>> getAllQuestions() {
        List<QuestionDTO> responseData = questionService.listAll()
                .stream()
                .map(QuestionDTO::buildQuestionDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista domande recuperata con successo.", responseData)
        );
    }
}
