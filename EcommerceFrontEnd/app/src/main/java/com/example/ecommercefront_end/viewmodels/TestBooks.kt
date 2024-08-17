package com.example.ecommercefront_end.viewmodels

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFormat
import com.example.ecommercefront_end.model.BookGenre
import com.example.ecommercefront_end.model.BookLanguage
import java.time.LocalDate


fun createTestBooks(): List<Book> {
    return listOf(
        Book(
            id = 1,
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            ISBN = "9780743273565",
            pages = 180,
            edition = "1st",
            format = BookFormat.HARDCOVER,
            genre = BookGenre.ROMANCE,
            language = BookLanguage.ENGLISH,
            publisher = "Scribner",
            age = 18,
            publishDate = LocalDate.of(1925, 4, 10),
            insertDate = LocalDate.now(),
            category = "Fiction",
            weight = 0.5,
            price = 10.99,
            stock = 15
        ),
        Book(
            id = 2,
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            ISBN = "9780061120084",
            pages = 281,
            edition = "1st",
            format = BookFormat.PAPERBACK,
            genre = BookGenre.MYSTERY,
            language = BookLanguage.ENGLISH,
            publisher = "J.B. Lippincott & Co.",
            age = 14,
            publishDate = LocalDate.of(1960, 7, 11),
            insertDate = LocalDate.now(),
            category = "Fiction",
            weight = 0.45,
            price = 7.99,
            stock = 10
        ),
        Book(
            id = 3,
            title = "1984",
            author = "George Orwell",
            ISBN = "9780451524935",
            pages = 328,
            edition = "1st",
            format = BookFormat.PAPERBACK,
            genre = BookGenre.CHILDREN,
            language = BookLanguage.ENGLISH,
            publisher = "Secker & Warburg",
            age = 16,
            publishDate = LocalDate.of(1949, 6, 8),
            insertDate = LocalDate.now(),
            category = "Science Fiction",
            weight = 0.4,
            price = 9.99,
            stock = 8
        ),
        Book(
            id = 4,
            title = "Pride and Prejudice",
            author = "Jane Austen",
            ISBN = "9780679783268",
            pages = 279,
            edition = "1st",
            format = BookFormat.HARDCOVER,
            genre = BookGenre.ROMANCE,
            language = BookLanguage.ENGLISH,
            publisher = "T. Egerton, Whitehall",
            age = 12,
            publishDate = LocalDate.of(1813, 1, 28),
            insertDate = LocalDate.now(),
            category = "Fiction",
            weight = 0.52,
            price = 12.99,
            stock = 20
        ),
        Book(
            id = 5,
            title = "The Hobbit",
            author = "J.R.R. Tolkien",
            ISBN = "9780547928227",
            pages = 310,
            edition = "1st",
            format = BookFormat.PAPERBACK,
            genre = BookGenre.FANTASY,
            language = BookLanguage.ENGLISH,
            publisher = "George Allen & Unwin",
            age = 10,
            publishDate = LocalDate.of(1937, 9, 21),
            insertDate = LocalDate.now(),
            category = "Fantasy",
            weight = 0.55,
            price = 14.99,
            stock = 25
        )
    )
}

