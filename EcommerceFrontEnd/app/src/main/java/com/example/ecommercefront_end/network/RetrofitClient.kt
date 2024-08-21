package com.example.ecommercefront_end.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object RetrofitClient {
    private const val BASE_URL = "https://192.168.1.29:8443/api/v1/"

    private const val SAMUELES_URL = "https://192.168.1.53:8081/api/v1/" //URL di Samuele S

    private val client: OkHttpClient

    init {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val allHostsValid = HostnameVerifier { _, _ -> true }

        client = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(allHostsValid)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SAMUELES_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                    .create()
            ))
            .build()
    }

    val cartApiService: CartApiService by lazy {
        retrofit.create(CartApiService::class.java)
    }

    val booksApiService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }

    val wishlistApiService: WishlistApiService by lazy {
        retrofit.create(WishlistApiService::class.java)
    }

    val wishlistItemApiService: WishlistItemApiService by lazy {
        retrofit.create(WishlistItemApiService::class.java)
    }


}