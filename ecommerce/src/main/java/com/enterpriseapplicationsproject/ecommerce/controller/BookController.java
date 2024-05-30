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
@RequestMapping("/books-api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BooksService booksService;

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> all() {
        return ResponseEntity.ok(booksService.getBookDto());
    }

    @GetMapping("/books/{idBook}")
    public ResponseEntity<BookDto> getById(@PathVariable("idBook") Long id) {
        BookDto b = booksService.getBookDtoById(id);
        if(b == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(b); // ritorna ok se la richiesta è andata a buon fine
    }

    public ResponseEntity<BookDto> add(@RequestBody BookDto bDto) {
        BookDto b = booksService.save(bDto);
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/books/{idBook}")
    public HttpStatus delete(@PathVariable("idBook") Long id) {
        booksService.deleteBook(id);
        return HttpStatus.OK;
    }
}
