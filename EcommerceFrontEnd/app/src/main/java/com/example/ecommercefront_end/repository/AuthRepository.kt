package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Credential
import com.example.ecommercefront_end.network.AuthApiService
import com.example.ecommercefront_end.network.UserApiService
import java.util.UUID

class AuthRepository (private val apiService : AuthApiService) {

    suspend fun loginUser(credentials: Credential) = apiService.login(credentials)
}