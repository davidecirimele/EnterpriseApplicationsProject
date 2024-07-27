package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.domain.PaymentStatus;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Transaction;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EcommerceSpecification {

    @Data
    public static class Filter{
        private Long userId;
        private Long OrderId;
        private Double minAmount;
        private Double maxAmount;
        private PaymentStatus paymentStatus;
        private LocalDate startDate;
        private LocalDate endDate;
        private String paymentMethodType;
    }

    public static Specification<Transaction> transactionFilter(Filter filter) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (filter.getUserId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("user").get("userId"), filter.getUserId()));
                }
                if (filter.getOrderId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("order").get("orderId"), filter.getOrderId()));
                }
                if (filter.getMinAmount() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
                }
                if (filter.getMaxAmount() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
                }
                if (filter.getPaymentStatus() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("paymentStatus"), filter.getPaymentStatus()));
                }
                if (filter.getStartDate() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
                }
                if (filter.getEndDate() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
                }
                if (filter.getPaymentMethodType() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("paymentMethod").get("type"), filter.getPaymentMethodType()));
                }

                return query.where(predicates.toArray(new Predicate[0])).orderBy(criteriaBuilder.desc(root.get("date"))).getRestriction();
            }
        };
    }






}

