package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.CardProvider
import com.example.ecommercefront_end.model.CardProviderAdapter
import com.example.ecommercefront_end.model.PaymentMethodType
import com.example.ecommercefront_end.model.PaymentMethodTypeAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitClient {
    private const val BASE_URL = "https://192.168.1.71:8443/api/v1/"

    private const val BASE2_URL = "https://192.168.1.7:8081/api/v1/"

    private const val BASE3_URL = "https://10.0.2.2:8081/api/v1/"

    private const val SAMUELES_URL = "https://192.168.143.117:8081/api/v1/" //URL di Samuele S

    const val DAVIDES_URL = "https://10.0.2.2:8080/api/v1/"

    val client: OkHttpClient by lazy {
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

        // Configura il logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logga il corpo della richiesta e della risposta
        }

        val allHostsValid = HostnameVerifier { _, _ -> true }
        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(allHostsValid)
            .addInterceptor(AuthInterceptor(SessionManager))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).registerTypeAdapter(PaymentMethodType::class.java,  PaymentMethodTypeAdapter() )
                    .registerTypeAdapter(CardProvider::class.java, CardProviderAdapter())
                    .create()
            ))
            .build()
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val adminApiService: AdminApiService by lazy {
        retrofit.create(AdminApiService::class.java)
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

    val groupApiService: GroupApiService by lazy {
        retrofit.create(GroupApiService::class.java)
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

    val checkoutApiService: CheckoutApiService by lazy {
        retrofit.create(CheckoutApiService::class.java)
    }

    val transactionApiService: TransactionApiService by lazy {
        retrofit.create(TransactionApiService::class.java)
    }
}

