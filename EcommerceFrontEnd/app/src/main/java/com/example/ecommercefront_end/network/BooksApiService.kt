package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.InsertCartItem
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.ShoppingCart
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
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

    @GET("/books/get/{idBook}")
    suspend fun getBook(@Path("idBook") idBook: Long)

    @GET("/books/getAll")
    suspend fun getAllBooks()

    @DELETE("/books/delete/{idBook}")
    suspend fun deleteBook(@Path("idBook") idBook: Long)

}