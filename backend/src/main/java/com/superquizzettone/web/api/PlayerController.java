package com.superquizzettone.web.api;

import com.superquizzettone.dto.QuizDTO;
import com.superquizzettone.dto.ResponseJSON;
import com.superquizzettone.service.quiz.QuizService;
import com.superquizzettone.service.utente.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final QuizService quizService;

    public PlayerController(UserService userService, QuizService quizService) {
        this.userService = userService;
        this.quizService = quizService;
    }

    @GetMapping("/quiz-list")
    public ResponseEntity<ResponseJSON<List<QuizDTO>>> listQuiz() {
        List<QuizDTO> response = QuizDTO.buildListDTOFromModel(this.quizService.listAllQuiz());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseJSON.success(200, "Lista pervenuta con successo", response));
    }

    @GetMapping("quiz/{id}")
    public ResponseEntity<ResponseJSON<QuizDTO>> getQuizById(@PathVariable Long id){
        QuizDTO response = QuizDTO.buildDTOFromModel(quizService.getQuizById(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseJSON.success(200, "Quiz pervenuto con successo", response));
    }

    /*

        createTrainingQuiz(Body quizDTO):
        endpoint: POST../player/create-training-quiz
        getQuiz(id Long):
        endpoint: GET ../player/quiz/{id}
        sendAnswers(Body answerDTO[]):
        endpoint: POST../player/send-answers
        receiveResults():
        endpoint: GET../player/receive-results
        quizHistoryRecord():
        endpoint: GET../player/quiz-history
        getGlobalClassification():
        endpoint: GET../player/global-classification
        dateRangeClassification(Body dataInizio Date, dataFine Date):
        endpoint: GET../player/date-range-classification
        findByExampleQuestions(Body questionDTO):
        endpoint: GET../player/search-question
        findByExampleQuiz(Body quizDTO):
        endpoint: GET../player/search-quiz
    */
}
