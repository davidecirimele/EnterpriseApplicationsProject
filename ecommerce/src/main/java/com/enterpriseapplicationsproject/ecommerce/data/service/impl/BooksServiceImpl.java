package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.BooksDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import com.enterpriseapplicationsproject.ecommerce.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BooksDao booksDao;

    private final ModelMapper modelMapper;

    @Override
    public Book getBookById(Long id) {
        return booksDao.findById(id).get();
    }

    @Override
    public BookDto getBookDtoById(Long id) {
        return booksDao.findById(id)
                .map(book -> modelMapper.map(book, BookDto.class))
                .orElse(null);
    }

    @Override
    public BookDto save(SaveBookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        book.setInsertDate(LocalDate.now());
        Book b = booksDao.save(book);
        return modelMapper.map(b, BookDto.class);
    }


    @Override
    public List<BookDto> getBookDto() {
        List <Book> books = booksDao.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }

    @Override
    public Collection<Book> getAll() {
        return booksDao.findAll();
    }

    @Override
    public BookDto deleteBook(Long id) {
        try {
            booksDao.deleteById(id);
            return getBookDtoById(id);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<BookDto> getAllSorted() {
        return booksDao.findAll(Sort.by(Sort.Order.asc("author").ignoreCase()
                .nullsFirst()).and(Sort.by(Sort.Order.asc("title").ignoreCase()
                        .nullsFirst()).and(Sort.by(Sort.Order.asc("price")
                        .nullsFirst()))))
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }


    @Override
    @Transactional
    public void downBookStock(Long id, int quantity) {
        Book book = booksDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setStock(book.getStock() - quantity);
        booksDao.save(book);
    }

    @Override
    @Transactional
    public void updateBookCover(Long id, String coverUrl) {
        Book book = booksDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
        book.setCoverUrl(coverUrl);
        booksDao.save(book);
    }
}
