package com.example.ecommercefront_end.network


import com.example.ecommercefront_end.model.QuantityCartItem
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.ShoppingCart
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface CartApiService {

    @POST("shopping-cart/cart/{userId}/{cartId}/{bookId}/insert")
    @RequiresAuth
    suspend fun insertItem(@Path("userId") userId: UUID, @Path("cartId") cartId: Long, @Path("bookId") bookId: Long, @Body quantityCartItem: QuantityCartItem)

    @GET("shopping-cart/get/{userId}")
    @RequiresAuth
    suspend fun getCart(@Path("userId") userId: UUID): Response<ShoppingCart?>

    @PUT("shopping-cart/cart/{userId}/{cartId}/{itemId}/edit-quantity")
    @RequiresAuth
    suspend fun updateQuantity(@Path("userId") userId: UUID, @Path("cartId") cartId: Long, @Path("itemId") itemId: Long, @Body quantityCartItem : QuantityCartItem)

    @DELETE("shopping-cart/cart/{userId}/{cartId}/{itemId}/remove")
    @RequiresAuth
    suspend fun removeItem(@Path("userId") userId: UUID, @Path("cartId") cartId: Long, @Path("itemId") itemId: Long)

}