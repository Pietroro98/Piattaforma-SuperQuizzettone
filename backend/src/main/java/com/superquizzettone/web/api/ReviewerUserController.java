package com.superquizzettone.web.api;

import com.superquizzettone.dto.MotivationDTO;
import com.superquizzettone.dto.QuestionDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.question.QuestionService;
import com.superquizzettone.service.utente.UserService;
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

}
