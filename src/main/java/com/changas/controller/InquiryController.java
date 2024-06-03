package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.question.AnswerQuestionRequest;
import com.changas.dto.question.CreateQuestionRequest;
import com.changas.dto.question.InquiryDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.inquiry.InquiryException;
import com.changas.exceptions.inquiry.SelfQuestionException;
import com.changas.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8100")
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "Create a new question")
    @PostMapping
    public ResponseEntity<ApiResponse<InquiryDTO>> createQuestion(@RequestBody CreateQuestionRequest request) throws CustomerNotAuthenticatedException, ChangaNotFoundException, SelfQuestionException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(inquiryService.createQuestion(request.changaId(), request.question())));
    }

    @Operation(summary = "Answer a question as provider")
    @PostMapping("/answer")
    public ResponseEntity<ApiResponse<InquiryDTO>> answerQuestion(@RequestBody AnswerQuestionRequest request) throws CustomerNotAuthenticatedException, InquiryException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(inquiryService.answerQuestion(request.questionId(), request.response())));
    }

    @Operation(summary = "Get a specific question by id")
    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<InquiryDTO>> getAQuestionById(@PathVariable Long questionId) throws InquiryException {
        return ResponseEntity.ok(ApiResponse.success(inquiryService.getQuestionById(questionId)));
    }

    @Operation(summary = "Returns all the inquiry for a given changa")
    @GetMapping("/changa/{changaId}")
    public ResponseEntity<ApiResponse<List<InquiryDTO>>> getAllQuestions(@PathVariable Long changaId) {
        return ResponseEntity.ok(ApiResponse.success(inquiryService.getAllQuestions(changaId)));
    }
}
