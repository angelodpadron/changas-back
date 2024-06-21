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
import com.changas.model.Changa;
import com.changas.model.Customer;
import com.changas.model.Review;
import com.changas.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReviewServiceTest {

    private final Customer reviewer = mock(Customer.class);
    private final Changa changa = mock(Changa.class);
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ChangaService changaService;
    @Mock
    private HiringTransactionService transactionService;
    @Mock
    private AuthService authService;
    @InjectMocks
    private ReviewService reviewService;

    private static CreateReviewRequest generateCreateReviewRequest() {
        CreateReviewRequest createReviewRequest = new CreateReviewRequest();
        createReviewRequest.setChangaId(1L);
        createReviewRequest.setRating(5);
        createReviewRequest.setComment("Comment");
        createReviewRequest.setPhotoUrl("Photo URL");
        return createReviewRequest;
    }

    @BeforeEach
    void setup() throws CustomerNotAuthenticatedException {
        when(reviewer.getId()).thenReturn(1L);
        when(changa.getId()).thenReturn(1L);

        when(authService.getCustomerAuthenticated()).thenReturn(reviewer);
    }

    @DisplayName("Reviewing a changa returns a review overview")
    @Test
    void reviewingAChangaReturnsAReviewingOverviewTest() throws ChangaNotFoundException, CustomerNotAuthenticatedException, ReviewException {
        CreateReviewRequest createReviewRequest = generateCreateReviewRequest();

        when(changaService.getChangaById(changa.getId())).thenReturn(changa);
        when(transactionService.hasHiredChanga(changa.getId(), reviewer.getId())).thenReturn(true);
        when(reviewRepository.findByChangaIdAndReviewerId(changa.getId(), reviewer.getId())).thenReturn(Optional.empty());

        ReviewDTO reviewDTO = reviewService.createReview(createReviewRequest);

        assertNotNull(reviewDTO);
        assertEquals(createReviewRequest.getRating(), reviewDTO.getRating());
        assertEquals(createReviewRequest.getComment(), reviewDTO.getComment());
        assertEquals(createReviewRequest.getPhotoUrl(), reviewDTO.getPhotoUrl());
    }

    @DisplayName("Cannot review a non-existent changa")
    @Test
    void cannotReviewANonExistentChangaTest() throws ChangaNotFoundException {
        CreateReviewRequest createReviewRequest = generateCreateReviewRequest();

        when(changaService.getChangaById(changa.getId())).thenThrow(ChangaNotFoundException.class);

        assertThrows(ChangaNotFoundException.class, () -> reviewService.createReview(createReviewRequest));
    }

    @DisplayName("Cannot review a changa that was not contacted beforehand")
    @Test
    void creatingAReviewReturnsDetailsTest() {
        CreateReviewRequest createReviewRequest = generateCreateReviewRequest();

        when(transactionService.hasHiredChanga(changa.getId(), reviewer.getId())).thenReturn(false);

        assertThrows(ReviewUnhiredChangaException.class, () -> reviewService.createReview(createReviewRequest));
    }

    @DisplayName("Cannot review an already reviewed changa")
    @Test
    void cannotReviewAnAlreadyReviewedChangaTest() {
        CreateReviewRequest createReviewRequest = generateCreateReviewRequest();

        when(reviewRepository.findByChangaIdAndReviewerId(changa.getId(), reviewer.getId())).thenReturn(Optional.of(new Review()));

        assertThrows(AlreadyReviewedException.class, () -> reviewService.createReview(createReviewRequest));
    }

    @DisplayName("Cannot review a changa without being authenticated")
    @Test
    void cannotReviewWithoutBeingAuthenticatedTest() throws CustomerNotAuthenticatedException {
        CreateReviewRequest createReviewRequest = generateCreateReviewRequest();

        when(authService.getCustomerAuthenticated()).thenThrow(CustomerNotAuthenticatedException.class);

        assertThrows(CustomerNotAuthenticatedException.class, () -> reviewService.createReview(createReviewRequest));
    }

    @DisplayName("Can retrieve a customer review")
    @Test
    void retrieveACustomerReviewTest() throws ReviewNotFoundException {
        Review review = generateReview();
        when(reviewRepository.findByChangaIdAndReviewerId(changa.getId(), reviewer.getId())).thenReturn(Optional.of(review));

        ReviewDTO reviewDTO = reviewService.getCustomerReviewFor(reviewer.getId(), changa.getId());

        assertNotNull(reviewDTO);
        assertEquals(review.getRating(), reviewDTO.getRating());
        assertEquals(review.getComment(), reviewDTO.getComment());
        assertEquals(review.getPhotoUrl(), reviewDTO.getPhotoUrl());
    }

    @DisplayName("Cannot retrieve an non-existent customer review")
    @Test
    void cannotRetrieveANonExistentCustomerReviewTest() {
        when(reviewRepository.findByChangaIdAndReviewerId(changa.getId(), reviewer.getId())).thenReturn(Optional.empty());
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getCustomerReviewFor(reviewer.getId(), changa.getId()));
    }

    private Review generateReview() {
        return Review.generateReview(changa, reviewer, 5, "Comment", "Photo URL");
    }

    @DisplayName("Get average reviews for a changa")
    @Test
    void getAverageReviewForAChangaTest() throws ChangaNotFoundException {
        when(changaService.getChangaById(changa.getId())).thenReturn(changa);
        when(reviewRepository.getAverageRateForChanga(changa.getId())).thenReturn(Optional.of(4.0));
        when(reviewRepository.getRateAmountForChanga(changa.getId())).thenReturn(Optional.of(2));

        AverageReview averageReview = reviewService.getAverageReview(1L);

        assertEquals(averageReview.average(),4.0);
        assertEquals(averageReview.amount(),2);

    }

    @DisplayName("Get all reviews for a changa")
    @Test
    void getAllReviewsForAChangaTest() throws ChangaNotFoundException {
        Review review5 = generateReview();
        Review review3 = Review.generateReview(changa, reviewer, 3, "Comment", "Photo URL");
        List<Review> reviews = new ArrayList<>();
        reviews.add(review3);
        reviews.add(review5);

        when(changaService.getChangaById(changa.getId())).thenReturn(changa);
        when(reviewRepository.getReviewsByChanga(changa.getId())).thenReturn(reviews);

        List<ReviewDTO> ret = reviewService.getReviewsForChanga(1L);

        assertEquals(ret.size(),reviews.size());
    }
}
