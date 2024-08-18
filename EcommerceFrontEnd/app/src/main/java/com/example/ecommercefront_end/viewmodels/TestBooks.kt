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
        ),
        Book(
        id = 6,
        title = "Harry Potter and the Sorcerer's Stone",
        author = "J.K. Rowling",
        ISBN = "9780590353403",
        pages = 309,
        edition = "1st",
        format = BookFormat.PAPERBACK,
        genre = BookGenre.FANTASY,
        language = BookLanguage.ENGLISH,
        publisher = "Bloomsbury",
        age = 9,
        publishDate = LocalDate.of(1997, 6, 26),
        insertDate = LocalDate.now(),
        category = "Fantasy",
        weight = 0.4,
        price = 8.99,
        stock = 30
        ),
        Book(
            id = 7,
            title = "The Lord of the Rings",author = "J.R.R. Tolkien",
            ISBN = "9780618057571",
            pages = 1178,
            edition = "1st",
            format = BookFormat.HARDCOVER,
            genre = BookGenre.FANTASY,
            language = BookLanguage.ENGLISH,
            publisher = "George Allen & Unwin",
            age = 14,
            publishDate = LocalDate.of(1954, 7, 29),
            insertDate = LocalDate.now(),
            category = "Fantasy",
            weight = 1.5,
            price = 24.99,
            stock = 15
        ),
        Book(
            id = 8,
            title = "The Catcher in the Rye",
            author = "J.D. Salinger",
            ISBN = "9780316769488",
            pages = 277,
            edition = "1st",
            format = BookFormat.PAPERBACK,
            genre = BookGenre.SCIENCE_FICTION,language = BookLanguage.ENGLISH,
            publisher = "Little, Brown and Company",
            age = 16,
            publishDate = LocalDate.of(1951, 7, 16),
            insertDate = LocalDate.now(),
            category = "Fiction",
            weight = 0.35,
            price = 6.99,
            stock = 12
        ),
        Book(
            id = 9,
            title = "And Then There Were None",
            author = "Agatha Christie",
            ISBN = "9780007136814",
            pages = 264,
            edition = "1st",
            format = BookFormat.PAPERBACK,
            genre = BookGenre.MYSTERY,
            language = BookLanguage.ENGLISH,
            publisher = "Collins Crime Club",
            age = 14,
            publishDate = LocalDate.of(1939, 11, 6),
            insertDate = LocalDate.now(),
            category = "Mystery",
            weight = 0.3,
            price = 5.99,
            stock = 18
        ),
        Book(
            id = 10,
            title = "The Da Vinci Code",
            author = "Dan Brown",
            ISBN = "9780307277671",
            pages = 454,
            edition = "1st",
            format = BookFormat.HARDCOVER,
            genre = BookGenre.THRILLER,
            language = BookLanguage.ENGLISH,
            publisher = "Doubleday",
            age = 16,
            publishDate = LocalDate.of(2003, 3, 18),
            insertDate = LocalDate.now(),
            category = "Thriller",
            weight = 0.6,
            price = 11.99,
            stock = 22
        )
    )
}

