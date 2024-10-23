package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    @Data
    public static class Filter {
        private Double weight;
        private Double price;
        private Double minPrice;
        private Double maxPrice;
        private Integer stock;
        private String title;
        private String author;
        private String ISBN;
        private Integer pages;
        private Integer minPages;
        private Integer maxPages;
        private String edition;
        private BookFormat format;
        private BookGenre genre;
        private BookLanguage language;
        private String publisher;
        private Integer age;
        private Integer minAge;
        private Integer maxAge;
        private LocalDate publishDate;
        private LocalDate minPublishDate;
        private LocalDate maxPublishDate;
        private Boolean available;
    }

    public static Specification<Book> bookFilter(BookSpecification.Filter filter) {

        return new Specification<Book>() {
            @Override
            public Specification<Book> and(Specification<Book> other) {
                return Specification.super.and(other);
            }

            @Override
            public Specification<Book> or(Specification<Book> other) {
                return Specification.super.or(other);
            }

            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (filter.getWeight() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), filter.getWeight()));
                }
                if (filter.getPrice() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("price"), filter.getPrice()));
                }
                if (filter.getMinPrice() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
                }
                if (filter.getMaxPrice() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
                }
                if (filter.getFormat() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("format"), filter.getFormat()));
                }
                if (filter.getEdition() != null) {
                    predicates.add(criteriaBuilder.like(root.get("edition"), "%" + filter.getEdition() + "%"));
                }
                if (filter.getLanguage() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("language"), filter.getLanguage()));
                }

                List<Predicate> orPredicates = new ArrayList<>();
                if (filter.getTitle() != null) {
                    orPredicates.add(criteriaBuilder.like(root.get("title"), "%" + filter.getTitle() + "%"));
                }
                if (filter.getAuthor() != null) {
                    orPredicates.add(criteriaBuilder.like(root.get("author"), "%" + filter.getAuthor() + "%"));
                }
                if (filter.getPublisher() != null) {
                    orPredicates.add(criteriaBuilder.like(root.get("publisher"), "%" + filter.getPublisher() + "%"));
                }
                if (filter.getISBN() != null) {
                    orPredicates.add(criteriaBuilder.equal(root.get("ISBN"), filter.getISBN()));
                }

                if (!orPredicates.isEmpty()) {
                    Predicate orPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
                    predicates.add(orPredicate);
                }

                if (filter.getGenre() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("genre"), filter.getGenre()));
                }
                if (filter.getStock() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stock"), filter.getStock()));
                }
                if (filter.getPublishDate() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("publishDate"), filter.getPublishDate()));
                }
                if (filter.getMinPublishDate() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("publishDate"), filter.getMinPublishDate()));
                }
                if (filter.getMaxPublishDate() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("publishDate"), filter.getMaxPublishDate()));
                }
                if (filter.getAge() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("age"), filter.getAge()));
                }
                if (filter.getMinAge() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"), filter.getMinAge()));
                }
                if (filter.getMaxAge() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age"), filter.getMaxAge()));
                }
                if (filter.getPages() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("pages"), filter.getPages()));
                }
                if (filter.getMinPages() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pages"), filter.getMinPages()));
                }
                if (filter.getMaxPages() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pages"), filter.getMaxPages()));
                }
                if (filter.getAvailable() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("available"), filter.getAvailable()));
                }

                return query.where(predicates.toArray(new Predicate[0])).getRestriction();
            }
        };
    }
}

