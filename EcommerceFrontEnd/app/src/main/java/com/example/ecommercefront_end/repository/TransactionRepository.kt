package com.example.ecommercefront_end.repository

import com.example.ecommercefront_end.model.Transaction
import com.example.ecommercefront_end.network.TransactionApiService
import retrofit2.Response
import java.util.UUID

class TransactionRepository(private val transactionApiService: TransactionApiService) {

    suspend fun getTransactions(userId: UUID): Response<List<Transaction>> {
        return transactionApiService.getTransactions(userId)
    }
}