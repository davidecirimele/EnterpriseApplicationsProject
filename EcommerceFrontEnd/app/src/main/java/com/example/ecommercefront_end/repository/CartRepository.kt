package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.network.CartApiService
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.ShoppingCart
import com.example.ecommercefront_end.model.UserId
import java.util.UUID

class CartRepository(
    private val apiService : CartApiService,


    ){

    suspend fun getCart(userId : UUID): Result<ShoppingCart?> {
        return try {
            println("Sto cercando il carrello")
            val response = apiService.getCart(userId)
            println("Risposta ricevuta $response")
            if (response.isSuccessful) {
                println("Carrello ricevuto")
                Result.success(response.body())
            } else {
                println("Errore: ${response.message()}")
                Result.failure(Exception(response.message()))

            }
        } catch ( e: Exception) {
            println("Errore: ${e.message}")
            e.printStackTrace()

            Result.failure(e)
        }
    }

    suspend fun updateQuantity( quantity : Int, userId: UserId) {
        val quantityCartItem = QuantityCartItem( userId, quantity)
        apiService.updateQuantity(quantityCartItem)
    }

    suspend fun removeItem( cartItemId: Long, userId: UserId){
        val cartItemId = CartItemId(cartItemId, userId)
        apiService.removeItem(cartItemId)

    }

}