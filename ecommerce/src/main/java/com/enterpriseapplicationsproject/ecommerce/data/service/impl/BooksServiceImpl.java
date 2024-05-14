package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.BooksDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    public Book save(Book book) {
        return booksDao.save(book);
    }

    @Override
    public List<BookDto> getBookDto() {
        return List.of(); // toDo
    }

    @Override
    public Collection<Book> getAll() {
        return booksDao.findAll();
    }
}
