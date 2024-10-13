package com.enterpriseapplicationsproject.ecommerce.data.dao;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BooksDao extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT MIN(b.price) FROM Book b")
    Double findMinPrice();
    @Query("SELECT MAX(b.price) FROM Book b")
    Double findMaxPrice();

    @Query("SELECT MIN(b.age) FROM Book b")
    Integer findMinAge();
    @Query("SELECT MAX(b.age) FROM Book b")
    Integer findMaxAge();

    @Query("SELECT MIN(b.pages) FROM Book b")
    Integer findMinPages();
    @Query("SELECT MAX(b.pages) FROM Book b")
    Integer findMaxPages();

    @Query("SELECT MIN(b.weight) FROM Book b")
    Double findMinWeight();
    @Query("SELECT MAX(b.weight) FROM Book b")
    Double findMaxWeight();

    @Query("SELECT MIN(b.publishDate) FROM Book b")
    LocalDate findMinPublicationYear();

}
