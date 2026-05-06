package com.superquizzettone.web.api;

import com.superquizzettone.dto.CategoryDTO;
import com.superquizzettone.dto.QuestionResponseDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.category.CategoryService;
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
    private final CategoryService categoryService;

    public OpenController(QuestionService questionService, CategoryService categoryService) {
        this.questionService = questionService;
        this.categoryService = categoryService;
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

    @GetMapping("/categories-list")
    public ResponseEntity<ResponseJSON<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> responseData = categoryService.listAll()
                .stream()
                .map(CategoryDTO::buildDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista categorie recuperate con successo.", responseData)
        );
    }
}
