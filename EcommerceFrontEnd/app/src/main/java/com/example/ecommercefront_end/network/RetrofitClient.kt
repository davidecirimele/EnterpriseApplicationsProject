package com.example.ecommercefront_end.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

object RetrofitClient {
    private const val BASE_URL = "http://localhost:8081/api/v1/"
    private const val SAMUELES_URL = "http://192.168.1.54:8081/api/v1/"

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SAMUELES_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val cartApiService: CartApiService by lazy {
        retrofit.create(CartApiService::class.java)
    }

    val booksApiService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }

}