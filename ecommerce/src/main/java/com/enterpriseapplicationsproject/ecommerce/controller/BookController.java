package com.enterpriseapplicationsproject.ecommerce.controller;

import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;
import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/books", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BooksService booksService;

    @GetMapping(consumes = "application/json", path = "/getAll")
    public ResponseEntity<List<BookDto>> all() {
        List<BookDto> books = booksService.getAllSorted();
        if (books.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json", path = "/get/{idBook}")
    public ResponseEntity<BookDto> getById(@PathVariable("idBook") Long id) {
        BookDto b = booksService.getBookDtoById(id);
        if(b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", path = "/add")
    public ResponseEntity<BookDto> add(@RequestBody BookDto bDto) {
        BookDto b = booksService.save(bDto);
        if (b == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }

    @DeleteMapping(consumes = "application/json", path = "/delete/{idBook}")
    public ResponseEntity<BookDto> delete(@PathVariable("idBook") Long id) {
        BookDto b = booksService.deleteBook(id);
        if (b == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(b, HttpStatus.OK);
    }
}
