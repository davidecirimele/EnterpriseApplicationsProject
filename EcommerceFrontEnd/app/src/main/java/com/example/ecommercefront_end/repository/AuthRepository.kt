package com.example.ecommercefront_end.repository


import com.example.ecommercefront_end.model.AccessToken
import com.example.ecommercefront_end.model.Credential
import com.example.ecommercefront_end.model.RefreshToken
import com.example.ecommercefront_end.model.RefreshTokenResponse
import com.example.ecommercefront_end.model.SaveUser
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.network.AuthApiService
import com.example.ecommercefront_end.network.UserApiService
import com.squareup.okhttp.Response
import java.util.UUID

class AuthRepository (private val apiService : AuthApiService) {

    suspend fun loginUser(credentials: Credential) = apiService.login(credentials)

    suspend fun registerUser(user: SaveUser) = apiService.registerUser(user)

    suspend fun registerAdmin(user: SaveUser) = apiService.registerAdmin(user)

    suspend fun validateToken(accessToken : AccessToken) = apiService.validateToken(accessToken)

    suspend fun refreshToken(userId: UUID, refreshToken: RefreshToken) = apiService.refreshToken(userId, refreshToken)
}

