package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.AccessToken
import com.example.ecommercefront_end.model.Credential
import com.example.ecommercefront_end.model.RefreshToken
import com.example.ecommercefront_end.model.RefreshTokenResponse
import com.example.ecommercefront_end.model.SaveUser
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserDetails
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface AuthApiService {

    @POST("auth/refreshToken")
    suspend fun refreshToken(@Body refreshToken: RefreshToken): Response<RefreshTokenResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body credentials: Credential): Response<Map<String, String>>

    @POST("auth/register")
    suspend fun registerUser(@Body user: SaveUser): Response<UserDetails>

    @POST("admin/register")
    suspend fun registerAdmin(@Body user: SaveUser): Response<UserDetails>

    @POST("auth/validate-token")
    suspend fun validateToken(@Body accessToken : AccessToken) : Response<AccessToken>

}