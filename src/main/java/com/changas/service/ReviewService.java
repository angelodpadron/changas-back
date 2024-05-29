package com.changas.service;

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
import com.changas.repository.ChangaRepository;
import com.changas.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ChangaRepository changaRepository;
    private final ChangaService changaService;
    private final HiringTransactionService transactionService;
    private final AuthService authService;


    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request) throws ReviewException, CustomerNotAuthenticatedException, ChangaNotFoundException {
        // TODO: find a better way to save review instead of relying on cascade

        Customer reviewer = authService.getCustomerAuthenticated();

        checkIfAlreadyReviewed(request.getChangaId(), reviewer.getId());
        checkIfCanReview(request.getChangaId(), reviewer.getId());

        Changa changa = changaService.getChangaById(request.getChangaId());

        Review review = Review.generateReview(changa, reviewer, request.getRating(), request.getComment(), request.getPhotoUrl());
        changa.getReviews().add(review);

        changaRepository.save(changa); // save by cascade

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
}
