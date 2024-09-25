package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.ReviewsService;
import com.enterpriseapplicationsproject.ecommerce.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/reviews")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewsService reviewsService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getById(@PathVariable("reviewId") Long id) {
        ReviewDto review = reviewsService.findById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> update(@PathVariable("reviewId") Long id, @RequestBody ReviewDto reviewDto) {
        ReviewDto updatedReview = reviewsService.update(id, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    @PostMapping()
    public ResponseEntity<ReviewDto> add(@RequestBody ReviewDto reviewDto) {
        ReviewDto savedReview = reviewsService.save(reviewDto);
        return ResponseEntity.ok(savedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable("reviewId") Long id) {
        reviewsService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByBook(@PathVariable("bookId") Long bookId) {
        List<ReviewDto> reviews = reviewsService.getReviewsByBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable("userId") UUID userId) {
        List<ReviewDto> reviews = reviewsService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }
}
