package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Book
import com.example.ecommercefront_end.model.Email
import com.example.ecommercefront_end.model.PhoneNumber
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserDetails
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface UserApiService {

    @GET("users/{id}")
    @RequiresAuth
    suspend fun getUser(@Path("id") id: UUID) : UserDetails?

    @PUT("users/{id}/change-email")
    @RequiresAuth
    suspend fun changeEmail(@Path("id") id: UUID, @Body email: Email)

    @PUT("users/{id}/change-phone-number")
    @RequiresAuth
    suspend fun changePhoneNumber(@Path("id") id: UUID, @Body phoneNumber: PhoneNumber)

}