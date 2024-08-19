package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.RequiresAuth
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class AuthInterceptor(
    private val sessionManager: SessionManager,
    private val authApiService: AuthApiService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requiresAuth = originalRequest.tag(Invocation::class.java)?.method()?.getAnnotation(
            RequiresAuth::class.java) != null

        if (!requiresAuth) {




        val accessToken = sessionManager.fetchAccessToken()

        val requestBuilder = originalRequest.newBuilder()
        accessToken?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }

        val requestWithAuth = requestBuilder.build()
        val response = chain.proceed(requestWithAuth)

        if (response.code() == 401) {


            val refreshToken = sessionManager.fetchRefreshToken()
            if (refreshToken != null) {
                val newTokensResponse = apiService.refreshToken(refreshToken).execute()
                if (newTokensResponse.isSuccessful) {
                    val newAccessToken = newTokensResponse.body()?.accessToken
                    val newRefreshToken = newTokensResponse.body()?.refreshToken

                    sessionManager.saveAccessToken(newAccessToken!!)
                    sessionManager.saveRefreshToken(newRefreshToken!!)

                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    return chain.proceed(newRequest)
                } else {
                    // Se il refresh token non funziona, logout o richiedi il login
                    sessionManager.clearSession()
                    // Puoi anche lanciare un'eccezione o gestire il logout in altro modo
                }
            }
        }

        return response
    }
        else {
            return chain.proceed(originalRequest)
        }
    }
}
