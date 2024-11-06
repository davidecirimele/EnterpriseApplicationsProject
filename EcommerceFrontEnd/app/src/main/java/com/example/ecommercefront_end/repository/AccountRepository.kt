package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Email
import com.example.ecommercefront_end.model.PasswordUser
import com.example.ecommercefront_end.model.PhoneNumber
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.network.AddressApiService
import com.example.ecommercefront_end.network.UserApiService
import java.util.UUID

class AccountRepository(private val apiService : UserApiService) {

    suspend fun getLoggedInUser(userId : UUID) = apiService.getUser(userId)

    suspend fun editEmail(userId: UUID, email: String) = apiService.changeEmail(userId, Email(email))

    suspend fun editPhoneNumber(userId: UUID, phoneNumber: String) = apiService.changePhoneNumber(userId, PhoneNumber(phoneNumber))

    suspend fun getUserOrders(userId: UUID) = apiService.getUserOrders(userId)

    suspend fun getPurchasedProducts(userId: UUID) = apiService.getPurchasedProducts(userId)

    suspend fun changePassword(userId: UUID, password: PasswordUser) = apiService.changePassword(userId, password)

    suspend fun logout(userId: UUID) = apiService.logout(userId)

    suspend fun deleteAccount(userId: UUID) = apiService.deleteAccount(userId)
}