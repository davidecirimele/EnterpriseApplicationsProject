package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.SessionManager
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
    private const val BASE_URL = "https://10.0.2.2:8080/"

    private const val SAMUELES_URL = "https://10.0.2.2:8081/api/v1/" //URL di Samuele S


    private val client: OkHttpClient by lazy {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<java.security.cert.X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val allHostsValid = HostnameVerifier { _, _ -> true }
        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(allHostsValid)
            .addInterceptor(AuthInterceptor(SessionManager))
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SAMUELES_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                    .create()
            ))
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val userApiService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    val wishlistApiService: WishlistApiService by lazy {
        retrofit.create(WishlistApiService::class.java)
    }

    val wishlistItemApiService: WishlistItemApiService by lazy {
        retrofit.create(WishlistItemApiService::class.java)
    }

    val cartApiService: CartApiService by lazy {
            retrofit.create(CartApiService::class.java)
    }

    val booksApiService: BooksApiService by lazy {
            retrofit.create(BooksApiService::class.java)
    }

    val addressApiService: AddressApiService by lazy {
        retrofit.create(AddressApiService::class.java)
    }
}

