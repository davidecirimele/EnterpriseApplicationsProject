package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Book
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class BooksService(private val client: HttpClient) {

    suspend fun getBooks(): List<Book>{
        return client.get("http://localhost:8080/api/v1/books/getAll").body()
    }
}