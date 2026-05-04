package com.superquizzettone.web.api;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.question.QuestionService;
import com.superquizzettone.service.quiz.QuizService;
import com.superquizzettone.web.api.exception.IdNotNullForInsertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/writer")
public class WriterController {

    private final QuestionService questionService;
    private final QuizService quizService;

    public WriterController(QuestionService questionService, QuizService quizService) {
        this.questionService = questionService;
        this.quizService = quizService;
    }

    @PostMapping("/create-question")
    public ResponseEntity<ResponseJSON<QuestionDTO>> createQuestion(@RequestBody @Valid QuestionDTO questionInput) {
        if (questionInput.getId() != null) {
            throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
        }

        Question questionInserita = questionService.insertNew(questionInput);
        QuestionDTO responseData = QuestionDTO.buildQuestionDTOFromModel(questionInserita);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseJSON.success(201, "Domanda creata con successo.", responseData));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<ResponseJSON<QuestionDTO>> modifyQuestion(@PathVariable Long id, @RequestBody @Valid QuestionDTO questionInput) {
        Question questionEsistente = questionService.getSingleElement(id);

        if (questionEsistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "Domanda non trovata per id: " + id));
        }

        questionInput.setId(id);
        Question questionAggiornata = questionService.update(questionInput);
        QuestionDTO responseData = QuestionDTO.buildQuestionDTOFromModel(questionAggiornata);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domanda aggiornata con successo.", responseData)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseJSON<QuestionDTO>> deleteQuestion(@PathVariable Long id) {
        Question questionEsistente = questionService.getSingleElement(id);
        if (questionEsistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "Domanda non trovata per id: " + id));
        }

        QuestionDTO responseData = QuestionDTO.buildQuestionDTOFromModel(questionEsistente);
        questionService.remove(id);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domanda eliminata con successo.", responseData)
        );
    }

    @GetMapping("/get-my-questions")
    public ResponseEntity<ResponseJSON<List<QuestionDTO>>> getMyQuestions() {
        List<QuestionDTO> responseData = questionService.getMyQuestions()
                .stream()
                .map(QuestionDTO::buildQuestionDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domande recuperate con successo.", responseData)
        );
    }

}
