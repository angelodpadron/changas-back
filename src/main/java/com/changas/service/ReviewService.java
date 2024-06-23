package com.changas.service;

import com.changas.dto.review.AverageReview;
import com.changas.dto.review.CreateReviewRequest;
import com.changas.dto.review.ReviewDTO;
import com.changas.exceptions.changa.ChangaNotFoundException;
import com.changas.exceptions.customer.CustomerNotAuthenticatedException;
import com.changas.exceptions.review.AlreadyReviewedException;
import com.changas.exceptions.review.ReviewException;
import com.changas.exceptions.review.ReviewNotFoundException;
import com.changas.exceptions.review.ReviewUnhiredChangaException;
import com.changas.mappers.ReviewMapper;
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.Review;
import com.changas.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ChangaService changaService;
    private final HiringTransactionService transactionService;
    private final AuthService authService;


    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request) throws ReviewException, CustomerNotAuthenticatedException, ChangaNotFoundException {
        Customer reviewer = authService.getCustomerAuthenticated();
        Changa changa = changaService.getChangaById(request.getChangaId());

        checkIfAlreadyReviewed(request.getChangaId(), reviewer.getId());
        checkIfCanReview(request.getChangaId(), reviewer.getId());

        Review review = Review.generateReview(changa, reviewer, request.getRating(), request.getComment(), request.getPhotoUrl());

        reviewRepository.save(review);

        return ReviewMapper.toReviewDTO(review);
    }

    private void checkIfCanReview(Long changaId, Long reviewerId) throws ReviewUnhiredChangaException {
        if (!transactionService.hasHiredChanga(changaId, reviewerId)) {
            throw new ReviewUnhiredChangaException();
        }
    }

    private void checkIfAlreadyReviewed(Long changaId, Long reviewerId) throws AlreadyReviewedException {
        Optional<Review> optionalReview = reviewRepository.findByChangaIdAndReviewerId(changaId, reviewerId);

        if (optionalReview.isPresent()) {
            throw new AlreadyReviewedException();
        }

    }

    public ReviewDTO getCustomerReviewFor(Long customerId, Long changaId) throws ReviewNotFoundException {
        return reviewRepository
                .findByChangaIdAndReviewerId(changaId, customerId)
                .map(ReviewMapper::toReviewDTO)
                .orElseThrow(() -> new ReviewNotFoundException(changaId));


    }

    public AverageReview getAverageReview(Long changaId) throws ChangaNotFoundException {
        Changa changa = changaService.getChangaById(changaId);
        Double average  = reviewRepository.getAverageRateForChanga(changa.getId()).orElse(0.0);
        Integer amount = reviewRepository.getRateAmountForChanga(changa.getId()).orElse(0);
        return new AverageReview(average, amount);
    }

    public List<ReviewDTO> getReviewsForChanga(Long changaId) throws ChangaNotFoundException {
        Changa changa = changaService.getChangaById(changaId);
        return reviewRepository
                .getReviewsByChanga(changa.getId())
                .stream()
                .map(ReviewMapper::toReviewDTO)
                .collect(Collectors.toList());
    }

    public AverageReview getCustomerAverageReview(Long customerId) {
        Double average = reviewRepository.getAverageRateForCustomer(customerId).orElse(0.0);
        Integer amount = reviewRepository.getRateAmountForCustomer(customerId).orElse(0);
        return new AverageReview(average, amount);

    }
}
