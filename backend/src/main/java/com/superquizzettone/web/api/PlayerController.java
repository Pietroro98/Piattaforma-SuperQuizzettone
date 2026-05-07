package com.superquizzettone.web.api;

import com.superquizzettone.dto.QuestionExampleDTO;
import com.superquizzettone.dto.QuizDTO;

import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.model.Question;
import com.superquizzettone.service.question.QuestionService;
import com.superquizzettone.service.quiz.QuizService;
import com.superquizzettone.service.utente.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final QuestionService questionService;

    @Autowired
    private final QuizService quizService;

    public PlayerController(UserService userService, QuestionService questionService, QuizService quizService) {
        this.userService = userService;
        this.questionService = questionService;
        this.quizService = quizService;
    }

    @GetMapping("/quiz-list")
    public ResponseEntity<ResponseJSON<List<QuizDTO>>> listQuiz() {
        List<QuizDTO> response = QuizDTO.buildListDTOFromModel(this.quizService.listAllQuiz());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseJSON.success(200, "Lista pervenuta con successo", response));
    }

    @GetMapping("quiz/{id}")
    public ResponseEntity<ResponseJSON<QuizDTO>> getQuiz(@PathVariable Long id){
        QuizDTO response = QuizDTO.buildDTOFromModel(quizService.getQuizById(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseJSON.success(200, "Quiz pervenuto con successo", response));
    }

    @GetMapping("/search-question")
    public ResponseEntity<ResponseJSON<List<QuestionExampleDTO>>> findByExampleQuestions (@Valid @RequestBody QuestionExampleDTO body){

        List<QuestionExampleDTO> result = QuestionExampleDTO.buildQuestionExampleDTOListFromModel(this.questionService.findByExample(body));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseJSON.success(200, "Lista recuperata con successo", result));
    }

    @GetMapping("/search-quiz")
    public ResponseEntity<ResponseJSON<List<QuizDTO>>> findByExampleQuiz(@Valid @RequestBody QuizDTO body){
        List<QuizDTO> result = QuizDTO.buildListDTOFromModel(this.quizService.findQuizByExample(body));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseJSON.success(200, "Lista recuperata con successo", result));
    }

    @PostMapping("/create-training-quiz")
    public ResponseEntity<ResponseJSON<QuizDTO>> createTrainingQuiz (@Valid @RequestBody QuizDTO body){
        return null;
    }
    /*
        createTrainingQuiz(Body quizDTO):
        endpoint: POST../player/create-training-quiz
        sendAnswers(Body answerDTO[]):
        endpoint: POST../player/send-answers
        receiveResults(QuizDTO body):
        endpoint: GET../player/receive-results
        quizHistoryRecord():
        endpoint: GET../player/quiz-history
        getGlobalClassification():
        endpoint: GET../player/global-classification
        dateRangeClassification(Body dataInizio Date, dataFine Date):
        endpoint: GET../player/date-range-classification
        findByExampleQuestions(Body questionDTO):
        endpoint: GET../player/search-question
    */
}
