package com.example.ecommercefront_end.network

import com.example.ecommercefront_end.SessionManager
import com.example.ecommercefront_end.model.RequiresAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class AuthInterceptor(
    private val sessionManager: SessionManager,
) : Interceptor {
    private var authApiService: AuthApiService? = null

    fun setAuthApiService(authApiService: AuthApiService) {
        this.authApiService = authApiService
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        setAuthApiService(RetrofitClient.authApiService)
        val authApiService = authApiService ?: throw IllegalStateException("AuthApiService is null")
        val originalRequest = chain.request()
        println("Intercepting request: ${originalRequest.url}")

        val requiresAuth = originalRequest.tag(Invocation::class.java)?.method()?.getAnnotation(
            RequiresAuth::class.java) != null
        println("Requires auth: $requiresAuth")

        if (requiresAuth) {

            val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJiaXJ0aGRhdGUiOiIxOTkwLTAxLTAxIiwidHlwZSI6ImFjY2Vzcy10b2tlbiIsInVzZXJJZCI6IjVlZWRkM2I0LTVjZDYtNGU4Ni04MGM3LTMyNThjMmZlNmYzMCIsInN1YiI6InV0ZW50ZUBwcm92YTEiLCJpYXQiOjE3MjQyNTE3ODIsImV4cCI6MTcyNDMzODE4Mn0.0Y5XGOl-WZf-F67fnOl9FjLa3ad1SmUjke6nkFeS3q8"
                //sessionManager.authToken

            val requestBuilder = originalRequest.newBuilder()
            accessToken?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }

            val requestWithAuth = requestBuilder.build()
            println("Request with auth: $requestWithAuth")
            val response = chain.proceed(requestWithAuth)
            println("Response code: ${response.code}")

            if (response.code == 401) {


                    val refreshToken = sessionManager.refreshToken
                    if (refreshToken != null) {
                        val newTokensResponse = runBlocking { withContext(Dispatchers.IO) {authApiService.refreshToken(refreshToken)  } }
                        if (newTokensResponse.isSuccessful) {
                            val newAccessToken = newTokensResponse.body()?.accessToken
                            val newRefreshToken = newTokensResponse.body()?.refreshToken

                            if (newAccessToken != null && newRefreshToken != null) {
                                sessionManager.saveAuthToken(newAccessToken)
                                sessionManager.saveRefreshToken(newRefreshToken)

                                val newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer $newAccessToken")
                                    .build()

                                response.close()

                                return chain.proceed(newRequest)
                            } else {
                                // Se il refresh token non funziona, logout o richiedi il login
                                sessionManager.clearSession()
                                // Puoi anche lanciare un'eccezione o gestire il logout in altro modo
                                //FIXME logout + redirect to login
                            }
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
