package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.CartItemId
import com.example.ecommercefront_end.model.InsertCartItem
import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.ShoppingCart
import retrofit2.http.*
import java.util.UUID

interface CartApiService {

    @POST("cart/insert")
    suspend fun insertItem(insertCartItem: InsertCartItem)

    @GET("/shopping-cart/user/{userId}")
    suspend fun getCart(@Path("userId") userId: UUID): ShoppingCart

    @PUT("/cart/edit-quantity")
    suspend fun updateQuantity(@Body quantityCartItem : QuantityCartItem)

    @DELETE("/cart/remove")
    suspend fun removeItem(@Body cartItemId: CartItemId)

}