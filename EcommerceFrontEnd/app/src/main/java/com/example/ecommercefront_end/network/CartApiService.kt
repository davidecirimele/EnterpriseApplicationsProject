package com.example.ecommercefront_end.network

import retrofit2.http.*
interface CartApiService {

    @POST("cart/insert")
    suspend fun insertCart(@Body cart: Cart): Cart



}