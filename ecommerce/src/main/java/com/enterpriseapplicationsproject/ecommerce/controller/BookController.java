package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/books", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BooksService booksService;

    @GetMapping(path = "/getAll")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDto>> getAll() {
        log.info("Received request for books/getAll");
        List<BookDto> books = booksService.getAllSorted();
        if (books.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // da testare se consumes va bene, dato che ha un corpo nella richiesta
    @GetMapping(consumes = "application/json", path = "/get/{idBook}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> getById(@PathVariable("idBook") Long id) {
        BookDto b = booksService.getBookDtoById(id);
        if(b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", path = "/add")
    @PreAuthorize("hasRole('ADMIN') and isAuthenticated()")
    public ResponseEntity<BookDto> add(@RequestBody SaveBookDto bDto) {
        BookDto b = booksService.save(bDto);
        if (b == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @DeleteMapping(consumes = "application/json", path = "/delete/{idBook}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> delete(@PathVariable("idBook") Long id) {
        BookDto b = booksService.deleteBook(id);
        if (b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }


    @PutMapping(path = "/{id}/updateCover")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateBookCover(@PathVariable Long id, @RequestParam String coverUrl) {
        booksService.updateBookCover(id, coverUrl);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
