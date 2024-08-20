package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface BooksService {

    Book getBookById(Long id);

    BookDto getBookDtoById(Long id);

    BookDto save(SaveBookDto bookDto);

    List<BookDto> getBookDto();

    Collection<Book> getAll();

    BookDto deleteBook(Long id);

    List<BookDto> getAllSorted();

    void downBookStock(Long id, int quantity);

    @Transactional
    void updateBookCover(Long id, String coverUrl);
}
