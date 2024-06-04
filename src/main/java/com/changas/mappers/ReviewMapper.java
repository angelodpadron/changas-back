package com.changas.mappers;

import com.changas.dto.review.ReviewDTO;
import com.changas.model.Review;

public class ReviewMapper {
    public static ReviewDTO toReviewDTO(Review review) {
        return ReviewDTO
                .builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .photoUrl(review.getPhotoUrl())
                .reviewer(CustomerMapper.toCustomerOverviewDTO(review.getReviewer()))
                .createdAt(review.getCreatedAt())
                .build();
    }
}
