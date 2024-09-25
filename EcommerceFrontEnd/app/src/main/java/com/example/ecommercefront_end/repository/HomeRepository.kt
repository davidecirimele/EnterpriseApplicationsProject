package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.network.BooksApiService
import java.util.UUID

class HomeRepository(private val apiService: BooksApiService) {

    suspend fun addBook(book: Book) {
        try {
            apiService.insertBook(book)
        } catch (e: Exception) {
            println("Errore durante l'aggiunta del libro: ${e.message}")
        }
    }

    suspend fun getBook(bookId: Long): Book? {
        return try {
            apiService.getBook(bookId)
        } catch (e: Exception) {
            println("Errore durante il recupero del libro: ${e.message}")
            null
        }
    }

    suspend fun getAllBooks(): List<Book> {
        return try {
            val books = apiService.getAllBooks()

            if (books.isEmpty()) {
                println("Nessun libro trovato")
            } else {
                println("libri presi")
            }
            books

        } catch (e: Exception) {
            println("Errore durante il recupero di tutti i libri: ${e.message}")
            emptyList() // Restituisci una lista vuota in caso di errore
        }
    }

    suspend fun deleteBook(bookId: Long) {
        try {
            apiService.deleteBook(bookId)
        } catch (e: Exception) {
            println("Errore durante la cancellazione del libro: ${e.message}")
        }
    }
}
