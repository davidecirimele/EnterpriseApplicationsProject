package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import com.enterpriseapplicationsproject.ecommerce.exception.BookNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    void saveBook(BookDto bookDto) throws IOException, IOException;

    void updateBookCover(Long bookId, MultipartFile coverImage) throws IOException, BookNotFoundException;
}
