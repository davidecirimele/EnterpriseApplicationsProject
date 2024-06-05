package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.BooksDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public BookDto save(BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);
        Book b = booksDao.save(book);
        return modelMapper.map(b, BookDto.class);
    }


    @Override
    public List<BookDto> getBookDto() {
        return List.of(); // toDo
    }

    @Override
    public Collection<Book> getAll() {
        return booksDao.findAll();
    }

    @Override
    public void deleteBook(Long id) {
        booksDao.deleteById(id);
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
}
