package com.enterpriseapplicationsproject.ecommerce.data.service.impl;

import com.enterpriseapplicationsproject.ecommerce.data.dao.BookSpecification;
import com.enterpriseapplicationsproject.ecommerce.data.dao.BooksDao;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.PriceDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.StockDto;
import com.enterpriseapplicationsproject.ecommerce.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public List<Book> getFilteredBooks(BookSpecification.Filter filter) {
        Specification<Book> spec = BookSpecification.bookFilter(filter);

        return booksDao.findAll(spec);
    }


    @Override
    public void saveBook(BookDto bookDto) throws IOException {
        Book book = new Book();

        MultipartFile imageFile = bookDto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, imageFile.getBytes());

            book.setImagePath(filePath.toString());
        }

        booksDao.save(book);
    }

    @Override
    public void updateBookCover(Long bookId, MultipartFile coverImage) throws IOException, BookNotFoundException {
        // Cerca il libro nel database
        Optional<Book> optionalBook = booksDao.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Libro non trovato");
        }

        Book book = optionalBook.get();

        // Salva la nuova immagine
        String fileName = coverImage.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, coverImage.getBytes());

        // Rimuovi l'immagine precedente dal filesystem
        if (book.getImagePath() != null) {
            Path oldImagePath = Paths.get(book.getImagePath());
            Files.deleteIfExists(oldImagePath);
        }

        // Aggiorna il percorso dell'immagine nel database
        book.setImagePath(filePath.toString());
        booksDao.save(book);
    }

    @Override
    public BookDto updatePrice(Long bookId, PriceDto newPrice) throws IOException, BookNotFoundException {
        Optional<Book> optionalBook = booksDao.findById(bookId);
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
        Optional<Book> optionalBook = booksDao.findById(bookId);
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
