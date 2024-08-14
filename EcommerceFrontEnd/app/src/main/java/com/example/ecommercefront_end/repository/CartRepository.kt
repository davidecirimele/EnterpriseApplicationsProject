package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.network.CartApiService
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.UserId

class CartRepository(
    private val apiService : CartApiService,
    private val userId: Long

){

    suspend fun getCart() = apiService.getCart(userId)

    suspend fun updateQuantity(cartItemId : Long, quantity : Int, userId: UserId) {
        val quantityCartItem = QuantityCartItem(cartItemId, userId, quantity)
        apiService.updateQuantity(quantityCartItem)
    }




}