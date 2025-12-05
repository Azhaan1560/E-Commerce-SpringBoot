package com.ESD.ecomm.controllers;

import com.ESD.ecomm.dto.review.ReviewRequestDTO;
import com.ESD.ecomm.dto.review.ReviewResponseDTO;
import com.ESD.ecomm.dto.review.ReviewUpdateDTO;
import com.ESD.ecomm.entities.Product;
import com.ESD.ecomm.entities.Review;
import com.ESD.ecomm.entities.User;
import com.ESD.ecomm.services.ProductService;
import com.ESD.ecomm.services.ReviewService;
import com.ESD.ecomm.services.UserService;
import com.ESD.ecomm.mappers.ReviewMappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            UserService userService,
                            ProductService productService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.productService = productService;
    }

    // Create new review
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.getProductById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (reviewService.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("User has already reviewed this product");
        }

        Review review = ReviewMappers.toReviewEntity(dto);
        review.setUser(user);
        review.setProduct(product);
        review = reviewService.saveReview(review);

        return new ResponseEntity<>(ReviewMappers.toResponseDTO(review), HttpStatus.CREATED);
    }

    // Get all reviews for a product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ReviewResponseDTO> reviews = reviewService.getReviewsByProduct(product)
                .stream()
                .map(ReviewMappers::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviews);
    }

    // Get all approved reviews for a product
    @GetMapping("/product/{productId}/approved")
    public ResponseEntity<List<ReviewResponseDTO>> getApprovedReviews(@PathVariable Long productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ReviewResponseDTO> reviews = reviewService.getApprovedReviewsByProduct(product)
                .stream()
                .map(ReviewMappers::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviews);
    }

    // Update review
    @PutMapping
    public ResponseEntity<ReviewResponseDTO> updateReview(@Valid @RequestBody ReviewUpdateDTO dto) {
        Review review = reviewService.getReviewsByUser(null) // optionally implement lookup by ID
                .stream()
                .filter(r -> r.getId().equals(dto.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found"));

        ReviewMappers.updateReviewFromDTO(dto, review);
        review = reviewService.saveReview(review);

        return ResponseEntity.ok(ReviewMappers.toResponseDTO(review));
    }

    // Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        Review review = reviewService.getReviewsByUser(null) // optionally implement lookup by ID
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewService.saveReview(review); // or implement delete method in service
        return ResponseEntity.noContent().build();
    }

    // Optional: Approve review
    @PatchMapping("/approve/{id}")
    public ResponseEntity<ReviewResponseDTO> approveReview(@PathVariable Long id) {
        Review review = reviewService.getReviewsByUser(null) // lookup by ID in service is recommended
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setIsApproved(true);
        review = reviewService.saveReview(review);

        return ResponseEntity.ok(ReviewMappers.toResponseDTO(review));
    }
}
