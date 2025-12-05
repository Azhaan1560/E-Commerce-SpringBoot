package com.ESD.ecomm.mappers;

import com.ESD.ecomm.dto.review.*;
import com.ESD.ecomm.entities.Review;

public class ReviewMappers {

    // ReviewRequestDTO -> Review entity
    public static Review toReviewEntity(ReviewRequestDTO dto) {
        if (dto == null) return null;

        return Review.builder()
                // Note: user and product entities should be set in service
                .rating(dto.getRating())
                .title(dto.getTitle())
                .comment(dto.getComment())
                .isVerifiedPurchase(dto.getIsVerifiedPurchase() != null ? dto.getIsVerifiedPurchase() : false)
                .build();
    }

    // Review entity -> ReviewResponseDTO
    public static ReviewResponseDTO toResponseDTO(Review review) {
        if (review == null) return null;

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUser() != null ? review.getUser().getId() : null)
                .productId(review.getProduct() != null ? review.getProduct().getId() : null)
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .isVerifiedPurchase(review.getIsVerifiedPurchase())
                .helpfulCount(review.getHelpfulCount())
                .isApproved(review.getIsApproved())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    // ReviewUpdateDTO -> update existing Review entity
    public static void updateReviewFromDTO(ReviewUpdateDTO dto, Review review) {
        if (dto == null || review == null) return;

        if (dto.getRating() != null) review.setRating(dto.getRating());
        if (dto.getTitle() != null) review.setTitle(dto.getTitle());
        if (dto.getComment() != null) review.setComment(dto.getComment());
        if (dto.getIsApproved() != null) review.setIsApproved(dto.getIsApproved());
    }
}
