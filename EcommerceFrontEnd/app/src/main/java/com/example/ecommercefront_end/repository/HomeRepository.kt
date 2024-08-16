package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.network.BooksApiService
import java.util.UUID

class HomeRepository(private val apiService : BooksApiService) {

    suspend fun addBook(book : Book) = apiService.insertBook(book)

    suspend fun getBook(bookId : Long) = apiService.getBook(bookId)

    suspend fun getAllBooks() = apiService.getAllBooks()

    suspend fun deleteBook( bookId: Long){
        apiService.deleteBook(bookId)
    }
}