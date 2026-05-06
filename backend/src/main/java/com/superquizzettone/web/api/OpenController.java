package com.superquizzettone.web.api;

import com.superquizzettone.dto.QuestionResponseDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.question.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/open-controller")
public class OpenController {

    private final QuestionService questionService;

    public OpenController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/all-questions")
    public ResponseEntity<ResponseJSON<List<QuestionResponseDTO>>> getAllQuestions() {
        List<QuestionResponseDTO> responseData = questionService.listAll()
                .stream()
                .map(QuestionResponseDTO::fromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista domande recuperata con successo.", responseData)
        );
    }

    @GetMapping("/get-my-questions")
    public ResponseEntity<ResponseJSON<List<QuestionResponseDTO>>> getMyQuestions() {

        List<Question> quest = questionService.getMyQuestions();

         List<QuestionResponseDTO> reponseData = quest
                .stream()
                .map(QuestionResponseDTO::fromModel)
                .toList();
        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domande recuperate con successo.", reponseData)
        );
    }
}
