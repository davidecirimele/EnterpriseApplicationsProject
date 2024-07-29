package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.dto.ReviewDto;

import java.util.List;
import java.util.UUID;

public interface ReviewsService {
    ReviewDto save(ReviewDto reviewDto);

    ReviewDto findById(Long id);

    ReviewDto update(Long id, ReviewDto reviewDto);

    void delete(Long id);

    List<ReviewDto> getReviewsByProduct(Long productId);

    List<ReviewDto> getReviewsByUser(UUID userId);
}
