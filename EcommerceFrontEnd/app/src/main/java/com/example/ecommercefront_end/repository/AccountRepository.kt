package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.network.UserApiService
import java.util.UUID

class AccountRepository(private val apiService : UserApiService) {

    suspend fun getLoggedInUser(userId : UUID) = apiService.getUser(userId)

}