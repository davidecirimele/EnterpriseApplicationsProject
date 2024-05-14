package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;

import java.util.Collection;
import java.util.List;

public interface BooksService {

    Book getBookById(Long id);

    Book save(Book book);

    List<BookDto> getBookDto();

    Collection<Book> getAll();


}
