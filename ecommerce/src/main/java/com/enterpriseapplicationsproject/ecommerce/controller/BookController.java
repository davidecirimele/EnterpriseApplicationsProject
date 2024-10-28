package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.dao.BookSpecification;
import com.enterpriseapplicationsproject.ecommerce.data.entities.Book;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.PriceDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.StockDto;
import com.enterpriseapplicationsproject.ecommerce.exception.BookNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/books", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BooksService booksService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @GetMapping(path = "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDto>> getAll() {
        log.info("Received request for books/getAll");
        List<BookDto> books = booksService.getAllSorted();
        if (books.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(path = "/get-catalogue")
    public ResponseEntity<List<BookDto>> getAllAvailable() {
        log.info("Received request for books/get-catalogue");
        List<BookDto> books = booksService.getAllAvailable();
        if (books.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/get/filter")
    public ResponseEntity<List<Book>> filterBooks(@RequestBody BookSpecification.Filter filter) {
        log.info("Received request for books/get/filter -> "+filter);
        List<Book> filteredBooks = booksService.getFilteredBooks(filter);
        return ResponseEntity.ok(filteredBooks);
    }

    @GetMapping("/get/max-price")
    public ResponseEntity<Double> getMaxPrice() {
        Double maxPrice = booksService.getMaxBookPrice();
        return ResponseEntity.ok(maxPrice);
    }

    @GetMapping("/get/min-price")
    public ResponseEntity<Double> getMinPrice() {
        Double minPrice = booksService.getMinBookPrice();
        return ResponseEntity.ok(minPrice);
    }

    @GetMapping("/get/max-age")
    public ResponseEntity<Integer> getMaxAge() {
        Integer maxAge = booksService.getMaxBookAge();
        return ResponseEntity.ok(maxAge);
    }

    @GetMapping("/get/min-age")
    public ResponseEntity<Integer> getMinAge() {
        Integer minAge = booksService.getMinBookAge();
        return ResponseEntity.ok(minAge);
    }

    @GetMapping("/get/max-pages")
    public ResponseEntity<Integer> getMaxPages() {
        Integer maxPages = booksService.getMaxBookPages();
        return ResponseEntity.ok(maxPages);
    }

    @GetMapping("/get/min-pages")
    public ResponseEntity<Integer> getMinPages() {
        Integer minPages = booksService.getMinBookPages();
        return ResponseEntity.ok(minPages);
    }

    @GetMapping("/get/max-weight")
    public ResponseEntity<Double> getMaxWeight() {
        Double maxWeight = booksService.getMaxBookWeight();
        return ResponseEntity.ok(maxWeight);
    }

    @GetMapping("/get/min-weight")
    public ResponseEntity<Double> getMinWeight() {
        Double minWeight = booksService.getMinBookWeight();
        return ResponseEntity.ok(minWeight);
    }

    @GetMapping("/get/min-publication-date")
    public ResponseEntity<LocalDate> getMinPublicationDate() {
        LocalDate minPublicationDate = booksService.getMinPublicationYear();
        return ResponseEntity.ok(minPublicationDate);
    }

    // da testare se consumes va bene, dato che ha un corpo nella richiesta
    @GetMapping(path = "/get/{idBook}")
    public ResponseEntity<BookDto> getById(@PathVariable("idBook") Long id) {
        BookDto b = booksService.getBookDtoById(id);
        if(b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @PostMapping(consumes = "multipart/form-data", path = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> addBook(@ModelAttribute  SaveBookDto book) throws IOException {
        BookDto savedBook = booksService.insertBook(book);
        if(savedBook == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @PutMapping(path = "/delete/{idBook}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> delete(@PathVariable("idBook") Long id) {
        BookDto b = booksService.deleteBook(id);
        if (b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @PutMapping(path = "/restore/{idBook}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> restore(@PathVariable("idBook") Long id) {
        BookDto b = booksService.restoreBook(id);
        if (b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @PostMapping("/{id}/update-cover")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookCover(@PathVariable Long id, @RequestParam("cover") MultipartFile coverImage) {
        try {
            booksService.updateBookCover(id, coverImage);
            return ResponseEntity.ok("Cover aggiornata con successo!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel salvataggio della cover.");
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro non trovato.");
        }
    }

    @PutMapping("/edit-price/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> updateBookPrice(@PathVariable Long id, @RequestBody PriceDto newPrice) {
        try {
            BookDto book = booksService.updatePrice(id, newPrice);
            return new ResponseEntity<>(book,HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/edit-stock/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> updateBookPrice(@PathVariable Long id, @RequestBody StockDto newStock) {
        try {
            BookDto book = booksService.updateStock(id, newStock);
            return new ResponseEntity<>(book,HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-cover/{filename}")
    public ResponseEntity<Resource> getCover(@PathVariable String filename) throws IOException {
        log.info("Received request for books/get-cover/"+filename);
        Path filePath = Paths.get(uploadDir, filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }
}
