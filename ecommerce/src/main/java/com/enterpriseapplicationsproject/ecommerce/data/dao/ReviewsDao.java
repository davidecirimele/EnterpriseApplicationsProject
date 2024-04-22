package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsDao extends JpaRepository<Review, Long> {
}
