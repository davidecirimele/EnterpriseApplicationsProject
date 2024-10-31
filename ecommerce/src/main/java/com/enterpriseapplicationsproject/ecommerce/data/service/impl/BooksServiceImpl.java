package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.BookSpecification;
import com.enterpriseapplicationsproject.ecommerce.data.dao.BooksDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.dto.*;
import com.enterpriseapplicationsproject.ecommerce.exception.BookNotFoundException;
import com.enterpriseapplicationsproject.ecommerce.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BooksDao booksDao;

    private final ModelMapper modelMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;


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
        Book b = booksDao.save(book);
        return modelMapper.map(b, BookDto.class);
    }


    @Override
    public List<BookDto> getBookDto() {
        List<Book> books = booksDao.findAll();
        return books.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }

    @Override
    public Collection<Book> getAll() {
        return booksDao.findAll();
    }

    @Override
    public List<BookDto> getAllAvailable() {

        Optional<List<Book>> optionalBooks = booksDao.findAllAvailable();

        if(optionalBooks.isPresent()){
            List<Book> books = optionalBooks.get();

            return books.stream().map(book -> modelMapper.map(book , BookDto.class)).toList();
        }
        return null;
    }

    @Override
    public BookDto deleteBook(Long id) {
        Optional<Book> optionalBook = booksDao.findByBookId(id);

        if(optionalBook.isPresent())
        {
            Book book = optionalBook.get();

            book.setAvailable(false);
            booksDao.save(book);

            return modelMapper.map(book, BookDto.class);
        }
        return null;
    }

    @Override
    public BookDto restoreBook(Long id) {
        Optional<Book> optionalBook = booksDao.findById(id);

        if(optionalBook.isPresent())
        {
            Book book = optionalBook.get();

            if(!book.isAvailable()) {
                book.setAvailable(true);
                booksDao.save(book);
            }

            return modelMapper.map(book, BookDto.class);
        }
        return null;
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
        Book book = booksDao.findByBookId(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        book.setStock(book.getStock() - quantity);
        booksDao.save(book);
    }

    @Override
    public List<Book> getFilteredBooks(BookSpecification.Filter filter) {
        Specification<Book> spec = BookSpecification.bookFilter(filter);

        return booksDao.findAll(spec);
    }


    @Override
    public BookDto insertBook(SaveBookDto bookDto) throws IOException {
        log.info("BOOK -> "+bookDto);
        Book book = modelMapper.map(bookDto, Book.class);

        MultipartFile imageFile = bookDto.getImage();

        String fileName = imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }


        try {
            log.info("Saving image to: " + filePath);
            Files.write(filePath, imageFile.getBytes());
            book.setImagePath(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error saving image: " + e.getMessage());
            throw new IOException("Error saving image: " + e.getMessage());
        }

        Book savedBook = booksDao.save(book);
        return modelMapper.map(savedBook, BookDto.class);
    }

    @Override
    public void updateBookCover(Long bookId, MultipartFile coverImage) throws IOException, BookNotFoundException {
        Optional<Book> optionalBook = booksDao.findByBookId(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Libro non trovato");
        }

        Book book = optionalBook.get();

        String fileName = coverImage.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, coverImage.getBytes());

        if (book.getImagePath() != null) {
            Path oldImagePath = Paths.get(book.getImagePath());
            Files.deleteIfExists(oldImagePath);
        }

        book.setImagePath(fileName);
        booksDao.save(book);
    }

    @Override
    public BookDto updatePrice(Long bookId, PriceDto newPrice) throws IOException, BookNotFoundException {
        Optional<Book> optionalBook = booksDao.findByBookId(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Libro non trovato");
        }

        Book book = optionalBook.get();

        if(newPrice.getPrice() > 0 && !Objects.equals(newPrice.getPrice(), book.getPrice())) {
            book.setPrice(newPrice.getPrice());
            Book savedBook = booksDao.save(book);
            return modelMapper.map(savedBook, BookDto.class);
        }
        return null;
    }

    @Override
    public BookDto updateStock(Long bookId, StockDto newStock) throws IOException, BookNotFoundException {
        Optional<Book> optionalBook = booksDao.findByBookId(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Libro non trovato");
        }

        Book book = optionalBook.get();

        if(newStock.getStock() > 0 && !Objects.equals(newStock.getStock(), book.getStock())) {
            book.setStock(newStock.getStock());
            Book savedBook = booksDao.save(book);
            return modelMapper.map(savedBook, BookDto.class);
        }
        return null;
    }

    @Override
    public Double getMaxBookPrice() {
        return booksDao.findMaxPrice();
    }

    @Override
    public Double getMinBookPrice() {
        return booksDao.findMinPrice();
    }

    @Override
    public Integer getMaxBookAge() {
        return booksDao.findMaxAge();
    }

    @Override
    public Integer getMinBookAge() {
        return booksDao.findMinAge();
    }

    @Override
    public Integer getMaxBookPages() {
        return booksDao.findMaxPages();
    }

    @Override
    public Integer getMinBookPages() {
        return booksDao.findMinPages();
    }

    @Override
    public Double getMaxBookWeight() {
        return booksDao.findMaxWeight();
    }

    @Override
    public Double getMinBookWeight() {
        return booksDao.findMinWeight();
    }

    @Override
    public LocalDate getMinPublicationYear(){return booksDao.findMinPublicationYear();}
}
