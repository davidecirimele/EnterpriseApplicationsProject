package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Email
import com.example.ecommercefront_end.model.PhoneNumber
import com.example.ecommercefront_end.network.AdminApiService
import com.example.ecommercefront_end.network.UserApiService
import java.util.UUID

class AdminRepository(private val apiService : AdminApiService) {

    suspend fun getAllUsers() = apiService.getAllUsers()

    suspend fun getAllOrders(page: Int, size: Int) = apiService.getAllOrders(page, size)
}