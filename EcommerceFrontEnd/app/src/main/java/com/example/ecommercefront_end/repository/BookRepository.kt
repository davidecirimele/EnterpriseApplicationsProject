package com.example.ecommercefront_end.repository

import android.util.Log
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.Price
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.model.Stock
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.network.BooksApiService

import java.util.UUID

class BookRepository(private val apiService : BooksApiService)  {

    suspend fun insertBook(book: SaveBook) = apiService.insertBook(book)

    suspend fun updatePrice(bookId: Long, newPrice: Price) = apiService.updatePrice(bookId, newPrice)

    suspend fun updateStock(bookId: Long, newStock : Stock) = apiService.updateStock(bookId, newStock)

    suspend fun getMaxPrice() = apiService.getMaxPrice()

    suspend fun getMinPrice() = apiService.getMinPrice()

    suspend fun getMaxAge() = apiService.getMaxAge()

    suspend fun getMinAge() = apiService.getMinAge()

    suspend fun getMaxPages() = apiService.getMaxPages()

    suspend fun getMinPages() = apiService.getMinPages()

    suspend fun getMaxWeight() = apiService.getMaxWeight()

    suspend fun getMinWeight() = apiService.getMinWeight()

    suspend fun getMinPublicationYear() = apiService.getMinPublicationYear()

    suspend fun getFilteredBooks(filter: BookFilter) = apiService.getFilteredBooks(filter)

    suspend fun getAllBooks() = apiService.getAllBooks()
}