package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.network.CartApiService
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.UserId
import java.util.UUID

class CartRepository(
    private val apiService : CartApiService,


){

    suspend fun getCart(userId : UUID) = apiService.getCart(userId)

    suspend fun updateQuantity( quantity : Int, userId: UserId) {
        val quantityCartItem = QuantityCartItem( userId, quantity)
        apiService.updateQuantity(quantityCartItem)
    }

    suspend fun removeItem( cartItemId: Long, userId: UserId){
        val cartItemId = CartItemId(cartItemId, userId)
        apiService.removeItem(cartItemId)

    }

}