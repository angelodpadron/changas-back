package com.changas.controller;

import com.changas.dto.ApiResponse;
import com.changas.dto.review.AverageReview;
import com.changas.dto.review.CreateReviewRequest;
import com.changas.dto.review.ReviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.review.ReviewException;
import com.changas.exceptions.review.ReviewNotFoundException;
import com.changas.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Post a review for a given changa")
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody CreateReviewRequest createReviewRequest) throws CustomerNotAuthenticatedException, ReviewException, ChangaNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(reviewService.createReview(createReviewRequest)));
    }

    @Operation(summary = "Retrieve customer review for a given changa")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getCustomerReview(@PathVariable Long customerId, @RequestParam Long changaId) throws ReviewNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getCustomerReviewFor(customerId, changaId)));
    }

    @Operation(summary = "Get average review for a given changa")
    @GetMapping("/overview/changa/{changaId}")
    public ResponseEntity<ApiResponse<AverageReview>> getAverageReview(@PathVariable Long changaId) throws ChangaNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAverageReview(changaId)));
    }

    @Operation(summary = "Get reviews for a given changa")
    @GetMapping("/changa/{changaId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsForChanga(@PathVariable Long changaId) throws ChangaNotFoundException {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsForChanga(changaId)));
    }


}
