package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/refreshToken")
    suspend fun refreshToken(refreshToken: String): Response<RefreshTokenResponse>

}