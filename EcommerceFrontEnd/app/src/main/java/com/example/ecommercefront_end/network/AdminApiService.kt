package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.OrderSummary
import com.example.ecommercefront_end.model.Page
import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.User
import com.example.ecommercefront_end.model.UserDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface AdminApiService {

    @GET("users/all-users")
    @RequiresAuth
    suspend fun getAllUsers() : Response<List<UserDetails>>?

    @GET("admin/all-orders")
    @RequiresAuth
    suspend fun getAllOrders(@Query("page") page: Int, @Query("size") size: Int) : Response<Page<OrderSummary>>?


}