package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BooksDao extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT MIN(b.price) FROM Book b WHERE b.available = true")
    Double findMinPrice();
    @Query("SELECT MAX(b.price) FROM Book b WHERE b.available = true")
    Double findMaxPrice();
    @Query("SELECT MIN(b.age) FROM Book b WHERE b.available = true")
    Integer findMinAge();
    @Query("SELECT MAX(b.age) FROM Book b WHERE b.available = true")
    Integer findMaxAge();

    @Query("SELECT MIN(b.pages) FROM Book b WHERE b.available = true")
    Integer findMinPages();
    @Query("SELECT MAX(b.pages) FROM Book b WHERE b.available = true")
    Integer findMaxPages();

    @Query("SELECT MIN(b.weight) FROM Book b WHERE b.available = true")
    Double findMinWeight();
    @Query("SELECT MAX(b.weight) FROM Book b WHERE b.available = true")
    Double findMaxWeight();

    @Query("SELECT MIN(b.publishDate) FROM Book b WHERE b.available = true")
    LocalDate findMinPublicationYear();

    @Query("SELECT b FROM Book b WHERE b.id = :bookId and b.available = true")
    Optional<Book> findByBookId(@Param("bookId") Long bookId);

    @Query("SELECT b FROM Book b WHERE b.available = true")
    Optional<List<Book>> findAllAvailable();
}
