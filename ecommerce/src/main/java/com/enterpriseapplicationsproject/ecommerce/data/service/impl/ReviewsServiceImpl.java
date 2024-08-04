package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.ReviewsDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Review;
import com.enterpriseapplicationsproject.ecommerce.data.service.ReviewsService;
import com.enterpriseapplicationsproject.ecommerce.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl implements ReviewsService {
    private final ReviewsDao reviewsDao;
    private final ModelMapper modelMapper;

    @Override
    public ReviewDto save(ReviewDto reviewDto) {
        Review review = modelMapper.map(reviewDto, Review.class);
        Review savedReview = reviewsDao.save(review);
        return modelMapper.map(savedReview, ReviewDto.class);
    }

    @Override
    public ReviewDto findById(Long id) {
        Review review = reviewsDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Review not found with id [%s]", id)));
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public ReviewDto update(Long id, ReviewDto reviewDto) {
        Review review = reviewsDao.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Review not found with id [%s]", id)));
        modelMapper.map(reviewDto, review);
        Review updatedReview = reviewsDao.save(review);
        return modelMapper.map(updatedReview, ReviewDto.class);
    }

    @Override
    public void delete(Long id) {
        reviewsDao.deleteById(id);
    }

    @Override
    public List<ReviewDto> getReviewsByBook(Long bookId) {
        List<Review> reviews = reviewsDao.findByBookId(bookId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> getReviewsByUser(UUID userId) {
        List<Review> reviews = reviewsDao.findByUserId(userId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }
}
