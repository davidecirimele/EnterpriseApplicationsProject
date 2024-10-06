package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.ShoppingCart

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface BooksApiService {

    @POST("books/add")
    suspend fun insertBook(insertBook: Book)

    @GET("books/get/{idBook}")
    suspend fun getBook(@Path("idBook") idBook: Long) : Book

    @GET("books/getAll")
    suspend fun getAllBooks() : List<Book>

    @DELETE("books/delete/{idBook}")
    suspend fun deleteBook(@Path("idBook") idBook: Long)
}