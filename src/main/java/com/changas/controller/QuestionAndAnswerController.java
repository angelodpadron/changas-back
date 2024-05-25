package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.question.QuestionDTO;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/q&a")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class QuestionAndAnswerController {

    private final QuestionService questionService;

    @Operation(summary = "Create a new question")
    @PostMapping("/question")
    public ResponseEntity<ApiResponse<QuestionDTO>> createQuestion(@RequestBody String question) throws CustomerNotAuthenticatedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(questionService.createQuestion(question)));
    }
}
