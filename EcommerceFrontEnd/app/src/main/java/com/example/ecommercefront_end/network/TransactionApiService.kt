package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.model.RequiresAuth
import com.example.ecommercefront_end.model.Transaction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

interface TransactionApiService {

    @GET("transactions/get/{userId}")
    @RequiresAuth
    suspend fun getTransactions(@Path("userId")userId: UUID): Response<List<Transaction>>


}