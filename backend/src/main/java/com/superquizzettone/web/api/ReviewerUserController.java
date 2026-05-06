package com.superquizzettone.web.api;

import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.dto.QuestionResponseDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.dto.ReviewQuestionRequestDTO;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.question.QuestionService;
import jakarta.validation.Valid;
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

    @Deprecated
    @PutMapping("/reject-question")
    public ResponseEntity<ResponseJSON<QuestionResponseDTO>> rejectQuestion(@RequestBody MotivationDTO motivationDTO){
        ReviewQuestionRequestDTO requestDTO = new ReviewQuestionRequestDTO();
        requestDTO.setStatus(motivationDTO.getStatus());
        requestDTO.setMotivationRejection(motivationDTO.getMotivationRejection());

        Question question = questionService.reviewQuestion(motivationDTO.getQuestion_id(), requestDTO);
        QuestionResponseDTO responseData = QuestionResponseDTO.fromModel(question);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Question rigettata con successo", responseData)
        );
    }

    @GetMapping("/questions/in-review")
    public ResponseEntity<ResponseJSON<List<QuestionResponseDTO>>> getQuestionsInReview() {
        List<QuestionResponseDTO> responseData = questionService.getQuestionsAvailableForReview()
                .stream()
                .map(QuestionResponseDTO::fromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista domande in review caricata correttamente", responseData)
        );
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<ResponseJSON<QuestionResponseDTO>> getQuestionDetails(@PathVariable Long id) {
        Question question = questionService.getSingleElement(id);

        if (question == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseJSON.error(404, "Domanda non trovata per id: " + id));
        }

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Domanda caricata correttamente", QuestionResponseDTO.fromModel(question))
        );
    }

    @PostMapping("/questions/{id}/claim")
    public ResponseEntity<ResponseJSON<QuestionResponseDTO>> claimQuestion(@PathVariable Long id) {
        Question question = questionService.claimForReview(id);
        QuestionResponseDTO responseData = QuestionResponseDTO.fromModel(question);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Question presa in carico correttamente", responseData)
        );
    }

    @PutMapping("/questions/{id}/review")
    public ResponseEntity<ResponseJSON<QuestionResponseDTO>> reviewQuestion(
            @PathVariable Long id,
            @RequestBody @Valid ReviewQuestionRequestDTO reviewQuestionRequestDTO) {
        Question question = questionService.reviewQuestion(id, reviewQuestionRequestDTO);
        QuestionResponseDTO responseData = QuestionResponseDTO.fromModel(question);

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Question revisionata correttamente", responseData)
        );
    }

    @GetMapping("/questions/my-claimed")
    public ResponseEntity<ResponseJSON<List<QuestionResponseDTO>>> myClaimedQuestions() {

        List<QuestionResponseDTO> responseData = questionService.getMyClaimedQuestions()
                .stream()
                .map(QuestionResponseDTO::fromModel)
                .toList();

        return ResponseEntity.ok(
                ResponseJSON.success(200, "Lista domande prese in carico caricata correttamente", responseData)
        );
    }
}
