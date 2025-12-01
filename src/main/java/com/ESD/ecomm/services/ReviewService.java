package com.ESD.ecomm.services;

import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.Review;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Get all reviews for a product
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    // Get approved reviews for a product
    public List<Review> getApprovedReviewsByProduct(Product product) {
        return reviewRepository.findByProductAndIsApprovedTrue(product);
    }

    // Get all reviews by a user
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    // Save or update a review
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Check if a user has already reviewed a product
    public boolean existsByUserAndProduct(User user, Product product) {
        return reviewRepository.existsByUserAndProduct(user, product);
    }
}
