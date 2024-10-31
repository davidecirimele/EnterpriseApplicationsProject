package com.enterpriseapplicationsproject.ecommerce.data.service;

import com.enterpriseapplicationsproject.ecommerce.data.dao.BookSpecification;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.PriceDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.StockDto;
import com.enterpriseapplicationsproject.ecommerce.exception.BookNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface BooksService {

    Book getBookById(Long id);

    BookDto getBookDtoById(Long id);

    BookDto save(SaveBookDto bookDto);

    List<BookDto> getBookDto();

    Collection<Book> getAll();

    List<BookDto> getAllAvailable();

    BookDto deleteBook(Long id);

    BookDto restoreBook(Long id);

    List<BookDto> getAllSorted();

    void downBookStock(Long id, int quantity);

    List<Book> getFilteredBooks(BookSpecification.Filter filter);

    BookDto insertBook(SaveBookDto bookDto) throws IOException, IOException;

    void updateBookCover(Long bookId, MultipartFile coverImage) throws IOException, BookNotFoundException;

    BookDto updatePrice(Long bookId, PriceDto newPrice) throws IOException, BookNotFoundException;

    BookDto updateStock(Long bookId, StockDto newStock) throws IOException, BookNotFoundException;

    Double getMaxBookPrice();

    Double getMinBookPrice();

    Integer getMaxBookAge();

    Integer getMinBookAge();

    Integer getMaxBookPages();

    Integer getMinBookPages();

    Double getMaxBookWeight();

    Double getMinBookWeight();

    LocalDate getMinPublicationYear();
}
