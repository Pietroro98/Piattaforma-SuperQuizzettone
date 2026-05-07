package com.superquizzettone.web.api;
import com.superquizzettone.dto.CreateQuestionRequestDTO;
import com.superquizzettone.dto.QuestionResponseDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.dto.UpdateQuestionRequestDTO;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.question.QuestionService;
import com.superquizzettone.service.quiz.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseJSON<QuestionResponseDTO>> createQuestion(@RequestBody @Valid CreateQuestionRequestDTO questionInput) {
        Question question = questionService.insertNew(questionInput.toModel());
        QuestionResponseDTO responseData = QuestionResponseDTO.fromModel(question);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseJSON.success(201, "Domanda creata con successo.", responseData));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<ResponseJSON<QuestionResponseDTO>> modifyQuestion(@PathVariable Long id, @RequestBody @Valid UpdateQuestionRequestDTO questionInput) {
        Question questionEsistente = questionService.getSingleElement(id);

        if (questionEsistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "Domanda non trovata per id: " + id));
        }

        Question questionToUpdate = questionInput.toModel();
        questionToUpdate.setId(id);
        Question questionAggiornata = questionService.update(questionToUpdate);
        QuestionResponseDTO responseData = QuestionResponseDTO.fromModel(questionAggiornata);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domanda aggiornata con successo.", responseData)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseJSON<QuestionResponseDTO>> deleteQuestion(@PathVariable Long id) {
        Question questionEsistente = questionService.getSingleElement(id);
        if (questionEsistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "Domanda non trovata per id: " + id));
        }

        QuestionResponseDTO responseData = QuestionResponseDTO.fromModel(questionEsistente);
        questionService.remove(id);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domanda eliminata con successo.", responseData)
        );
    }

}
