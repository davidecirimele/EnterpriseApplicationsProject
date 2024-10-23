package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.BookFilter
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.Price
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.SaveBook
import com.example.ecommercefront_end.model.ShoppingCart
import com.example.ecommercefront_end.model.Stock
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDate
import java.util.UUID

interface BooksApiService {

    @POST("books/add")
    @RequiresAuth
    suspend fun insertBook(@Body book: SaveBook) : Response<Book>

    @GET("books/get/{idBook}")
    suspend fun getBook(@Path("idBook") idBook: Long) : Book

    @GET("books/getAll")
    @RequiresAuth
    suspend fun getAllBooks() : Response<List<Book>>

    @GET("books/get/max-price")
    suspend fun getMaxPrice() : Response<Double>

    @GET("books/get/min-price")
    suspend fun getMinPrice() : Response<Double>

    @GET("books/get/max-age")
    suspend fun getMaxAge() : Response<Int>

    @GET("books/get/min-age")
    suspend fun getMinAge() : Response<Int>

    @GET("books/get/max-pages")
    suspend fun getMaxPages() : Response<Int>

    @GET("books/get/min-pages")
    suspend fun getMinPages() : Response<Int>

    @GET("books/get/max-weight")
    suspend fun getMaxWeight() : Response<Double>

    @GET("books/get/min-weight")
    suspend fun getMinWeight() : Response<Double>

    @GET("books/get/min-publication-date")
    suspend fun getMinPublicationYear() : Response<LocalDate>

    @DELETE("books/delete/{idBook}")
    @RequiresAuth
    suspend fun deleteBook(@Path("idBook") idBook: Long)

    @POST("books/get/filter")
    suspend fun getFilteredBooks(@Body filter: BookFilter) : Response<List<Book>>

    @PUT("books/edit-price/{bookId}")
    @RequiresAuth
    suspend fun updatePrice(@Path("bookId") bookId: Long, @Body newPrice: Price): Response<Book>

    @PUT("books/edit-stock/{bookId}")
    @RequiresAuth
    suspend fun updateStock(@Path("bookId") bookId: Long, @Body newStock: Stock): Response<Book>
}