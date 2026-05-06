package com.superquizzettone.web.api;

import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.question.QuestionService;
import com.superquizzettone.service.utente.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviewer")
public class ReviewerUserController {

    private final QuestionService questionService;

    public ReviewerUserController( QuestionService questionService){
        this.questionService = questionService;
    }

    @GetMapping("/list-questions")
    public ResponseEntity<ResponseJSON<List<QuestionDTO>>> questionList(){

        List<QuestionDTO> responseData = questionService.listAll()
                .stream()
                .map(QuestionDTO::buildQuestionDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista di questions caricata correttamente", responseData)
        );
    }

    @PutMapping("/reject-question")
    public ResponseEntity<ResponseJSON<QuestionDTO>> rejectQuestion(@RequestBody MotivationDTO motivationDTO){

        Question question = questionService.rejectQuestion(motivationDTO);
        QuestionDTO responseData = QuestionDTO.buildQuestionDTOFromModel(question);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Question rigettata con successo", responseData)
        );
    }

    @GetMapping("/questions/in-review")
    public ResponseEntity<ResponseJSON<List<QuestionDTO>>> getQuestionsInReview() {
        List<QuestionDTO> responseData = questionService.getQuestionsAvailableForReview()
                .stream()
                .map(QuestionDTO::buildQuestionDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista domande in review caricata correttamente", responseData)
        );
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<ResponseJSON<QuestionDTO>> getQuestionDetails(@PathVariable Long id) {
        Question question = questionService.getSingleElement(id);

        if (question == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "Domanda non trovata per id: " + id));
        }

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domanda caricata correttamente", QuestionDTO.buildQuestionDTOFromModel(question))
        );
    }

    @PostMapping("/questions/{id}/claim")
    public ResponseEntity<ResponseJSON<QuestionDTO>> claimQuestion(@PathVariable Long id) {
        Question question = questionService.claimForReview(id);
        QuestionDTO responseData = QuestionDTO.buildQuestionDTOFromModel(question);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Question presa in carico correttamente", responseData)
        );
    }

    @GetMapping("/questions/my-claimed")
    public ResponseEntity<ResponseJSON<List<QuestionDTO>>> myClaimedQuestions() {

        List<QuestionDTO> responseData = questionService.getMyClaimedQuestions()
                .stream()
                .map(QuestionDTO::buildQuestionDTOFromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista domande prese in carico caricata correttamente", responseData)
        );
    }
}
