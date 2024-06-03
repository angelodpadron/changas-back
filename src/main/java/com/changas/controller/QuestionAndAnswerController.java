package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.changa.ChangaOverviewDTO;
import com.changas.dto.question.QuestionDTO;
import com.changas.exceptions.QuestionAndAnswer.QuestionNotFoundException;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/queries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100") 
public class QuestionAndAnswerController {

    private final QuestionService questionService;

    @Operation(summary = "Create a new question")
    @PostMapping("/question/{changa_id}")
    public ResponseEntity<ApiResponse<QuestionDTO>> createQuestion(@PathVariable Long changa_id,@RequestBody String question) throws CustomerNotAuthenticatedException, ChangaNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(questionService.createQuestion(changa_id, question)));
    }

    @Operation(summary = "Get a specific question by id")
    @GetMapping("question/{question_id}")
    public ResponseEntity<ApiResponse<QuestionDTO>> getAQuestionById(@PathVariable Long question_id) throws QuestionNotFoundException {
        return  ResponseEntity.ok(ApiResponse.success(questionService.getQuestionById(question_id)));
    }

    @Operation(summary = "Returns all the questions")
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getAllQuestions() {
        return ResponseEntity.ok(ApiResponse.success(questionService.getAllQuestions()));
    }
}
