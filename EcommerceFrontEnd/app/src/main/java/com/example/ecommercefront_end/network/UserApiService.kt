package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.User
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface UserApiService {

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: UUID) : User?

}