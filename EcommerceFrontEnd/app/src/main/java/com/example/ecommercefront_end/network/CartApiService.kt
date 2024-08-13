package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.ShoppingCart
import retrofit2.http.*
interface CartApiService {

    @POST("cart/insert")
    suspend fun insertItem(insertCartItem: InsertCartItem)

    @GET("/shopping-cart/user/{userId}")
    suspend fun getCart(@Path("userId") userId: Long): ShoppingCart

    @PUT("/cart/edit-quantity")
    suspend fun updateQuantity(@Body quantityCartItem : QuantityCartItem)


}