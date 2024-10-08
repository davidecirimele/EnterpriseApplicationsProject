package com.example.ecommercefront_end.repository

import android.util.Log
import com.example.ecommercefront_end.model.Address
import com.example.ecommercefront_end.model.SaveAddress
import com.example.ecommercefront_end.model.UserId
import com.example.ecommercefront_end.network.BooksApiService

import java.util.UUID

class BookRepository(private val apiService : BooksApiService)  {

    suspend fun getMaxPrice() = apiService.getMaxPrice()

    suspend fun getMinPrice() = apiService.getMinPrice()

    suspend fun getMaxAge() = apiService.getMaxAge()

    suspend fun getMinAge() = apiService.getMinAge()

    suspend fun getMaxPages() = apiService.getMaxPages()

    suspend fun getMinPages() = apiService.getMinPages()

}