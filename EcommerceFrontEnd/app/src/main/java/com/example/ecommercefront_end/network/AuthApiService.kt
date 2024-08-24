package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Credential
import com.example.ecommercefront_end.model.RefreshTokenResponse
import com.example.ecommercefront_end.model.SaveUser
import com.example.ecommercefront_end.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {

    @POST("/auth/refreshToken")
    suspend fun refreshToken(refreshToken: String): Response<RefreshTokenResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/auth/login")
    suspend fun login(@Body credentials: Credential): Response<Map<String, String>>

    @POST("/api/v1/auth/register")
    suspend fun register(@Body user: SaveUser): Response<User>

    @POST("/auth/validate-token")
    suspend fun validateToken(@Body accessToken : String): Response<Boolean>

}